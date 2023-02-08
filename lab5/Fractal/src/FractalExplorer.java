import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int size;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;

    public FractalExplorer(int size) {
        this.size = size;
        this.display = new JImageDisplay(size, size);
        this.fractal = new Mandelbrot();
        this.range = new Rectangle2D.Double();

        fractal.getInitialRange(range);
    }
    public void createAndShowGUI (){
        display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Mandelbrot fractal");
        JButton resetBtn = new JButton("Reset");

        frame.add(display, BorderLayout.CENTER);
//        frame.add(resetBtn, BorderLayout.SOUTH);

        ButtonHandler resetHandler = new ButtonHandler();
        resetBtn.addActionListener(resetHandler);

        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        JPanel panel = new JPanel();
        panel.add(resetBtn);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal(){
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                double xCord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
                double yCord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);

                int iterations = fractal.numIterations(xCord, yCord);

                if (iterations == -1){
                    display.paintPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) iterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    display.paintPixel(x, y, rgbColor);
                }
                display.repaint();
            }
        }
    }

    private class ButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Reset")){
                fractal.getInitialRange(range);
                drawFractal();
            }
        }
    }

    private class MouseHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
            int y = e.getY();
            double yCord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);

            fractal.recenterAndZoomRange(range, xCord, yCord, 0.5);
            drawFractal();
        }
    }

    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(400);
        displayExplorer.createAndShowGUI();;
        displayExplorer.drawFractal();
    }
}
