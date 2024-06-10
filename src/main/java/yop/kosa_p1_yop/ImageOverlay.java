package yop.kosa_p1_yop;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageOverlay {
    public static void main(String[] args) {
        // 최종 이미지 생성
        BufferedImage result = null;

        try {
            // 폴더에 있는 이미지 파일들의 경로
            String folderPath = System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Images/toppings";

            // 폴더 내의 모든 이미지를 합치기
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            if (files == null) {
                throw new IOException("Failed to list files in the directory: " + folderPath);
            }

            for (File file : files) {
                if (file.getName().endsWith(".png")) { // 필요한 파일 형식으로 필터링할 수 있음
                    BufferedImage image = ImageIO.read(file);

                    if (result == null) {
                        // 첫 번째 이미지일 경우에는 바로 result에 할당
                        result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = result.createGraphics();
                        g2d.drawImage(image, 0, 0, null);
                        g2d.dispose();
                    } else {
                        // 두 번째 이미지부터는 result에 이미지를 오버레이
                        Graphics2D g2d = result.createGraphics();
                        g2d.drawImage(image, 0, 0, null);
                        g2d.dispose();
                    }
                }
            }

            // 최종 이미지 저장
            String outputPath = System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Images/toppingsresult.png";
            ImageIO.write(result, "png", new File(outputPath));
            System.out.println("이미지 생성이 완료되었습니다. (" + outputPath + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
