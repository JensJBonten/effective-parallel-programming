import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ConvexHull {
    public int n;
    public int[] x;
    public int[] y;
    public IntList hull;
    public int MAX_X;
    public int MAX_Y;

    // lagt til antall kjerner og maks nivå for parallell rekursjon.
    private final int numberOfCores;
    private final int maxParallelLevel;

    public ConvexHull(IntList amountOfPoints, int[] x, int[] y) {
        n = amountOfPoints.size();
        this.x = x;
        this.y = y;
        hull = new IntList();

        numberOfCores = Runtime.getRuntime().availableProcessors();
        maxParallelLevel = findMaxParallelLevel(numberOfCores);

        findMaxIndexes();
    }

    /**
     * Finding the convexHull sequentially. The method finds the point with the lowest x value,
     * the point with the highest x value, and then computes the convexHull recursively.
     */
    public void findConvexHullSeq() {
        hull.clear();

        if (n == 0) {
            return;
        }

        if (n == 1) {
            hull.add(0);
            return;
        }

        int minX = findMinXIndex();
        int maxX = MAX_X;

        hull.add(maxX);
        hull.append(findHullSequential(maxX, minX));
        hull.add(minX);
        hull.append(findHullSequential(minX, maxX));
    }

    /**
     * The parallel version of finding the convex hull. The method uses the same recursive logic
     * as the sequential method, but starts parallel tasks for the upper levels of the recursion tree.
     */
    public void findConvexHullParallel() {
        hull.clear();

        if (n == 0) {
            return;
        }

        if (n == 1) {
            hull.add(0);
            return;
        }

        int minX = findMinXIndex();
        int maxX = MAX_X;

        try (ForkJoinPool pool = new ForkJoinPool(numberOfCores)) {
            HullTask rightSide = new HullTask(maxX, minX, 0);
            HullTask leftSide = new HullTask(minX, maxX, 0);

            rightSide.fork();
            IntList leftHull = pool.invoke(leftSide);
            IntList rightHull = rightSide.join();

            hull.add(maxX);
            hull.append(rightHull);
            hull.add(minX);
            hull.append(leftHull);
        }
    }

    /**
     * Old method name kept so Main and older testing code can still call the parallel solution
     * with a start and end parameter. The convex hull itself still has to use all points.
     *
     * @param start old start parameter.
     * @param end   old end parameter.
     */
    public void findConvexHullParallel(int start, int end) {
        findConvexHullParallel();
    }

    /**
     * Computes the points on the hull between p1 and p2 sequentially.
     * The method returns the points between p1 and p2, but not p1 and p2 themselves.
     *
     * @param p1 first point on the hull.
     * @param p2 second point on the hull.
     * @return the points found between p1 and p2.
     */
    private IntList findHullSequential(int p1, int p2) {
        IntList result = new IntList();


        int farthestPoint = findFarthestPoint(p1, p2);

        if (farthestPoint == -1) {
            return findPointsOnLine(p1, p2);
        }

        result.append(findHullSequential(p1, farthestPoint));
        result.add(farthestPoint);
        result.append(findHullSequential(farthestPoint, p2));

        return result;
    }

    /**
     * FORBEDRET: egen intern task-klasse for parallell rekursjon.
     */
    private class HullTask extends RecursiveTask<IntList> {
        private final int p1;
        private final int p2;
        private final int level;

        public HullTask(int p1, int p2, int level) {
            this.p1 = p1;
            this.p2 = p2;
            this.level = level;
        }

        @Override
        protected IntList compute() {
            //Stopper å lage nye tråder etter et visst nivå.
            if (level >= maxParallelLevel) {
                return findHullSequential(p1, p2);
            }

            IntList result = new IntList();
            int farthestPoint = findFarthestPoint(p1, p2);

            if (farthestPoint == -1) {
                return findPointsOnLine(p1, p2);
            }

            HullTask leftTask = new HullTask(p1, farthestPoint, level + 1);
            HullTask rightTask = new HullTask(farthestPoint, p2, level + 1);

            leftTask.fork();
            IntList rightHull = rightTask.compute();
            IntList leftHull = leftTask.join();

            result.append(leftHull);
            result.add(farthestPoint);
            result.append(rightHull);

            return result;
        }
    }

    /**
     * Finds the point with the largest negative distance from the line p1-p2.
     * Since the actual distance is not needed, the method uses the line value directly.
     *
     * @param p1 first point on the line.
     * @param p2 second point on the line.
     * @return the index of the point furthest away from the line, or -1 if no point is outside.
     */
    private int findFarthestPoint(int p1, int p2) {
        long smallestDistance = 0;
        int farthestPoint = -1;

        for (int i = 0; i < n; i++) {
            long distance = lineValue(p1, p2, i);

            if (distance < smallestDistance) {
                smallestDistance = distance;
                farthestPoint = i;
            }
        }

        return farthestPoint;
    }

    /**
     * Finds points that are on the same line between p1 and p2.
     * These points are also part of the convex hull and have to be added in the correct order.
     *
     * @param p1 first point on the line.
     * @param p2 second point on the line.
     * @return the points on the line between p1 and p2.
     */
    private IntList findPointsOnLine(int p1, int p2) {
        IntList pointsOnLine = new IntList();
        int[] points = new int[n];
        int amountOfPoints = 0;

        for (int i = 0; i < n; i++) {
            if (i != p1 && i != p2 && lineValue(p1, p2, i) == 0 && isBetween(p1, p2, i)) {
                points[amountOfPoints++] = i;
            }
        }

       // sorterer punkter på samme linje slik at hull-rekkefølgen blir riktig.
        sortPointsByDistanceFromP1(points, amountOfPoints, p1);

        for (int i = 0; i < amountOfPoints; i++) {
            pointsOnLine.add(points[i]);
        }

        return pointsOnLine;
    }

    /**
     * This method returns the value of the line equation for a point.
     * Negative values means that the point is on the right side of the line from p1 to p2.
     *
     * @param p1 first point on the line.
     * @param p2 second point on the line.
     * @param p3 point being checked.
     * @return line equation value for p3.
     */
    private long lineValue(int p1, int p2, int p3) {
        // Bruker long for å redusere risiko for overflow ved store n.
        long a = y[p1] - y[p2];
        long b = x[p2] - x[p1];
        long c = ((long) y[p2] * x[p1]) - ((long) y[p1] * x[p2]);

        return (a * x[p3]) + (b * y[p3]) + c;
    }

    /**
     * Checks if p3 is placed between p1 and p2.
     *
     * @param p1 first point.
     * @param p2 second point.
     * @param p3 point being checked.
     * @return true if p3 is between p1 and p2.
     */
    private boolean isBetween(int p1, int p2, int p3) {
        return x[p3] >= Math.min(x[p1], x[p2]) && x[p3] <= Math.max(x[p1], x[p2])
                && y[p3] >= Math.min(y[p1], y[p2]) && y[p3] <= Math.max(y[p1], y[p2]);
    }

    private void sortPointsByDistanceFromP1(int[] points, int amountOfPoints, int p1) {
        Integer[] sortedPoints = new Integer[amountOfPoints];

        for (int i = 0; i < amountOfPoints; i++) {
            sortedPoints[i] = points[i];
        }

        Arrays.sort(sortedPoints, (point1, point2) -> Long.compare(distanceSquared(p1, point1), distanceSquared(p1, point2)));

        for (int i = 0; i < amountOfPoints; i++) {
            points[i] = sortedPoints[i];
        }
    }

    private long distanceSquared(int p1, int p2) {
        long xDistance = x[p1] - x[p2];
        long yDistance = y[p1] - y[p2];

        return (xDistance * xDistance) + (yDistance * yDistance);
    }

    private int findMinXIndex() {
        int minX = 0;

        for (int i = 1; i < n; i++) {
            if (x[i] < x[minX] || (x[i] == x[minX] && y[i] < y[minX])) {
                minX = i;
            }
        }

        return minX;
    }

    private void findMaxIndexes() {
        MAX_X = 0;
        MAX_Y = 0;

        for (int i = 1; i < n; i++) {
            if (x[i] > x[MAX_X] || (x[i] == x[MAX_X] && y[i] > y[MAX_X])) {
                MAX_X = i;
            }

            if (y[i] > y[MAX_Y] || (y[i] == y[MAX_Y] && x[i] > x[MAX_Y])) {
                MAX_Y = i;
            }
        }
    }

    private int findMaxParallelLevel(int cores) {
        int level = 0;
        int threads = 1;

        while (threads < cores) {
            threads *= 2;
            level++;
        }

        return level;
    }

    /**
     * This method returns the orientation of the points. If the cross product of these points is 0, then the three points
     * are collinear, which means they are lying in a single line. if the cross product is 1, the points in question are
     * arranged clockwise, else the points are oriented counterclockwise.
     *
     * @return 0, 1 or 2 depending on whether the orientation of the points is counterclockwise, clockwise or collinear.
     */
    public int orientation(int x1, int y1, int x2, int y2, int x3, int y3) {
        long crossProduct = ((long) y2 - y1) * (x3 - x2) - ((long) x2 - x1) * (y3 - y2);

        if (crossProduct == 0) {
            return 0;
        } else if (crossProduct > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * @return the intList containing the convex Hull.
     */
    public IntList getHullIndices() {
        return hull;
    }
}