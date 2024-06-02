package yop.kosa_p1_yop;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageCombiner {

    public static void main(String[] args) {
        //testing method
        try {
            List<String> imagePaths = List.of("image1.jpg", "image2.jpg", "image3.jpg");
            BufferedImage combinedImage = combineImages(imagePaths);
            ImageIO.write(combinedImage, "jpg", new File("combined_image.jpg"));
            System.out.println("Images combined successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage combineImages(List<String> imagePaths) throws IOException {
        BufferedImage[] images = new BufferedImage[imagePaths.size()];
        int maxWidth = 0;
        int totalHeight = 0;

        // Load images and find maximum width and total height
        for (int i = 0; i < imagePaths.size(); i++) {
            images[i] = ImageIO.read(new File(imagePaths.get(i)));
            maxWidth = Math.max(maxWidth, images[i].getWidth());
            totalHeight += images[i].getHeight();
        }

        // Create a blank canvas with the maximum width and total height
        BufferedImage combinedImage = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = combinedImage.createGraphics();

        // Draw each image onto the combined image
        int currentY = 0;
        for (BufferedImage image : images) {
            g2d.drawImage(image, 0, currentY, null);
            currentY += image.getHeight();
        }
        g2d.dispose();

        return combinedImage;
    }
}
