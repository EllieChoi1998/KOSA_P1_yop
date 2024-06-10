package yop.kosa_p1_yop;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageOverlay {

    public static void main(String[] args) {
        //test method
        // 실제 사용할 때에는 ImageOverlay.overlay_and_store() 로 불러들여야 함.
        String[] topping_names = {"베이컨토핑", "불고기토핑"};
        boolean result = overlay_and_store(topping_names, "testcombine_test");
        if(result == true){
            System.out.println("Successfully created");
        }
    }

    public static boolean overlay_and_store(String[] topping_names, String customPizzaName_customerId){
        // 최종 이미지 생성
        BufferedImage result = null;

        try {
            // 폴더에 있는 이미지 파일들의 경로
            String folderPath = System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Images/toppings/";
            String basePath = System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Images/standard_pizzas/치즈피자.png";
            File[] files = new File[topping_names.length+1];
            files[0] = new File(basePath);
            for(int i = 1; i < topping_names.length+1 ; i++){
                File tmp = new File(folderPath + topping_names[i-1] + ".png");
                files[i] = tmp;
            }

            if (files == null) {
                throw new IOException("Failed to list files in the directory: " + folderPath);
            }

            for (File file : files) {

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

            // 최종 이미지 저장
            String outputPath = System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Custom_Pizzas/"+customPizzaName_customerId+"1.png";
            ImageIO.write(result, "png", new File(outputPath));
            System.out.println("이미지 생성이 완료되었습니다. (" + outputPath + ")");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
