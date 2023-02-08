import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent {

    private BufferedImage image;

    public JImageDisplay(int weight, int height) {
        this.image = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(weight, height));
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    // Draw image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    // Set background black
    public void clearImage(){
        image.setRGB(0, 0, 0);
    }

    // Paint pixel
    public void paintPixel(int x, int y, int rgbColor){
        image.setRGB(x, y, rgbColor);
    }
}
