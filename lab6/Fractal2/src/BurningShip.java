import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double x, double y) {
        double zReal = 0;
        double zImaginary = 0;
        int iteration = 0;

        while (zReal * zReal + zImaginary * zImaginary < 4){

            if (iteration == MAX_ITERATIONS) return -1;

            double zRealUpdated = zReal * zReal - zImaginary * zImaginary + x;
            double zImaginaryUpdated = Math.abs(2 * zReal * zImaginary) + y;

            zReal = zRealUpdated;
            zImaginary = zImaginaryUpdated;
            iteration++;
        }
        return iteration;
    }

    @Override
    public String toString() {
        return "Burning ship";
    }
}
