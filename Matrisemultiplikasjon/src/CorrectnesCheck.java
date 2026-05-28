public class CorrectnesCheck {
    private static final double EPSILON = 0.000001;

    /**
     * Check if the multiplication has been done correctly.
     * Floating point numbers can differ slightly, so the method uses a small tolerance.
     *
     * @param correctSolution is the sequential not transposed multiplication matrix as a check.
     * @param test is the matrix that's just been multiplied that we want to check.
     * @param n size of the matrices.
     * @return either true or false depending on if the test matrix has been multiplied correctly.
     */
    static boolean checkWithLoop(double[][] correctSolution, double[][] test, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(correctSolution[i][j] - test[i][j]) > EPSILON) {
                    return false;
                }
            }
        }

        return true;
    }
}