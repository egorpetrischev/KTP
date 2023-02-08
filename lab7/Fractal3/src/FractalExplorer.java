import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FractalExplorer {
    private int size;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;
    private int rowsRemaining;
    private JButton resetBtn;
    private JButton saveBtn;
    private JComboBox<FractalGenerator> fractalBox;

    public FractalExplorer(int size) {
        this.size = size;
        this.display = new JImageDisplay(size, size);
        this.fractal = new Mandelbrot();
        this.range = new Rectangle2D.Double();

        fractal.getInitialRange(range);
    }
    public void createAndShowGUI (){
        JFrame frame = new JFrame("Mandelbrot fractal");

        display.setLayout(new BorderLayout());
        frame.add(display, BorderLayout.CENTER);

        resetBtn = new JButton("Reset");
        saveBtn = new JButton("Save");
        JPanel southPanel = new JPanel();
        southPanel.add(resetBtn);
        southPanel.add(saveBtn);
        frame.add(southPanel, BorderLayout.SOUTH);

        resetBtn.addActionListener(new ButtonHandler());
        saveBtn.addActionListener(new ButtonHandler());

        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        JPanel northPanel = new JPanel();
        fractalBox = new JComboBox<>();
        fractalBox.addItem(new Mandelbrot());
        fractalBox.addItem(new Tricorn());
        fractalBox.addItem(new BurningShip());
        JLabel label = new JLabel("Fractal:");
        northPanel.add(label);
        northPanel.add(fractalBox);
        frame.add(northPanel, BorderLayout.NORTH);

        fractalBox.addActionListener(new ButtonHandler());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal(){
        enableUI(false);
        rowsRemaining = size;
        for (int x = 0; x < size; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }

    }

    public void enableUI(boolean b) {
        saveBtn.setEnabled(b);
        resetBtn.setEnabled(b);
        fractalBox.setEnabled(b);
    }


    private class ButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() instanceof JComboBox){
                JComboBox<String> source = (JComboBox<String>) e.getSource();
                fractal = (FractalGenerator) source.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();

            } else {
                String command = e.getActionCommand();

                switch (command){
                    case "Reset":
                        fractal.getInitialRange(range);
                        drawFractal();
                        break;
                    case "Save":
                        JFileChooser save = new JFileChooser();
                        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("PNG Images", "png");

                        save.setFileFilter(fileFilter);
                        save.setAcceptAllFileFilterUsed(false);

                        int saved = save.showSaveDialog(display);
                        if (saved == JFileChooser.APPROVE_OPTION){
                            java.io.File file = save.getSelectedFile();

                            if (!file.getAbsolutePath().endsWith(".png"))
                                file = new java.io.File(save.getSelectedFile() + ".png");

                            try{
                                BufferedImage displayImage = display.getImage();
                                javax.imageio.ImageIO.write(displayImage, "png", file);
                                JOptionPane.showMessageDialog(display, "Successfully saved");
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(display, ex.getMessage(), "Cannot save Image",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                }
            }
        }

    }
    private class MouseHandler extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (rowsRemaining != 0) return;

            int x = e.getX();
            double xCord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
            int y = e.getY();
            double yCord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);

            fractal.recenterAndZoomRange(range, xCord, yCord, 0.5);
            drawFractal();
        }
    }
    private class FractalWorker extends SwingWorker<Object, Object> {
        private int y;

        public FractalWorker(int y) {
            this.y = y;

        }

        @Override
        public Object doInBackground() throws Exception {
            int[] colors = new int[size];
            for (int i = 0; i < size; i++) {

                int iterations = fractal.numIterations(FractalGenerator.getCoord(range.x, range.x + range.width, size, i),
                        FractalGenerator.getCoord(range.y, range.y + range.height, size, y));

                if (iterations == -1) {
                    colors[i] = 0;
                } else {
                    float hue = 0.7f + (float) iterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    colors[i] = rgbColor;
                }

                display.paintPixel(i, y, colors[i]);
            }
            return null;
        }

        @Override
        protected void done() {
            display.repaint(0, 0, y, size, 1);
            rowsRemaining--;
            if (rowsRemaining == 0)
                enableUI(true);
        }

    }

    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
