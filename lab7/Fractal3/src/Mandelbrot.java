import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    @Override
    public int numIterations(double x, double y) {
        double zReal = 0;
        double zImaginary = 0;
        int iteration = 0;

        while (zReal * zReal + zImaginary * zImaginary < 4){

            if (iteration == MAX_ITERATIONS) return -1;

            double zRealUpdated = zReal * zReal - zImaginary * zImaginary + x;
            double zImaginaryUpdated = 2 * zReal * zImaginary + y;

            zReal = zRealUpdated;
            zImaginary = zImaginaryUpdated;
            iteration++;
        }
        return iteration;
    }

    @Override
    public String toString() {
        return "Mandelbrot";
    }
}
