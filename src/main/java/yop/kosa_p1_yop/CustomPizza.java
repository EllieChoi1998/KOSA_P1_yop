package yop.kosa_p1_yop;

import javafx.scene.chart.PieChart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.List;

public class CustomPizza {
    private static String custom_name;
    private static String user_id;
    private static String[] base;
    private static Map<String, Boolean> toppings;
    private static boolean is_large;
    private static double price;
    private static double[] base_infos;
    private CustomPizza() {
        // private 생성자로 인스턴스화 방지
    }

    public static void createCustomPizza(String[] base, boolean is_large){
        CustomPizza.base = base;
        CustomPizza.toppings = new HashMap<>();
        CustomPizza.is_large = is_large;

        CustomPizza.base_infos = calculate_base_infos();
        CustomPizza.price = CustomPizza.base_infos[6];
    }

    public static boolean addToppings(String toppingName) {
        if (toppings == null) {
            toppings = new HashMap<>();
        }

        if (!toppings.containsKey(toppingName) || !toppings.get(toppingName)) {
            toppings.put(toppingName, true);
            System.out.println("Topping added: " + toppingName); // 추가된 토핑 이름 출력
            System.out.println(toppings);
            return true;
        } else {
            System.out.println("Topping already exists: " + toppingName); // 이미 존재하는 토핑
            return false;
        }
    }

    public static boolean deleteToppings(String toppingName) {
        if (toppings != null && toppings.get(toppingName) != null && toppings.get(toppingName)) {
            toppings.remove(toppingName);
            System.out.println("Topping removed: " + toppingName); // 제거된 토핑 이름 출력
            System.out.println(toppings);
            return true;
        } else {
            System.out.println("Topping not found: " + toppingName); // 찾을 수 없는 토핑
            return false;
        }
    }

    public static String[] getBase(){
        return base;
    }

    public static Map<String, Boolean> getToppings() {
        return toppings;
    }

    public static void resetPizza() {
        custom_name = null;
        user_id = null;
        base = null;
        toppings = null;
        is_large = false;
        price = 0.0;
    }

    public static boolean setCustomName(String custom_name) {
        CustomPizza.custom_name = custom_name;
        boolean validate_name = CustomPizza.name_validation(custom_name);
        if(validate_name){

            Set<String> toppingNames = CustomPizza.getToppings().keySet();
            List<String> tmp_names = new ArrayList<>();
            for (String name : toppingNames){
                if(CustomPizza.getToppings().get(name) != null && CustomPizza.getToppings().get(name)){
                    tmp_names.add(name);
                }
            }
            String[] names = new String[tmp_names.size()];

            for (int i = 0; i < tmp_names.size(); i++){
                names[i] = tmp_names.get(i);
            }

            int new_id = CustomPizza.create_new_pizza_in_database();
            if (new_id != -1){
                boolean success = CustomPizza.create_new_pizza_ingredient_relation_in_database(new_id);
                if(success){
                    String new_pizza_name = String.valueOf(new_id);
                    boolean imagesuccess = CustomPizza.overlay_and_store(names, new_pizza_name);
                    if(imagesuccess){
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public static boolean create_new_pizza_ingredient_relation_in_database(int new_id){
        Connection conn = null;
        PreparedStatement pstmt = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            for (String toppingname : CustomPizza.getToppings().keySet()){
                String sql = "INSERT INTO ingredient_item (pizza_id, ing_id) " +
                        "VALUES (?, (SELECT id FROM ingredient WHERE name = ?))";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, new_id);
                pstmt.setString(2, toppingname);
                pstmt.executeUpdate();
                DatabaseConnect.commit(conn);
            }

            for (String basename : CustomPizza.getBase()) {
                String sql = "INSERT INTO ingredient_item (pizza_id, ing_id) " +
                        "VALUES (?, (SELECT id FROM ingredient WHERE name = ?))";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, new_id);
                pstmt.setString(2, basename);
                pstmt.executeUpdate();
                DatabaseConnect.commit(conn);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeConnection(conn);
        }
        return false;
    }

    public static boolean name_validation(String custom_name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            // PreparedStatement를 사용하여 SQL 인젝션 방지
            String sql = "SELECT COUNT(*) FROM pizza WHERE customer_id = ? AND name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, CustomerUser.getId());
            pstmt.setString(2, custom_name);
            rs = pstmt.executeQuery();

            // 결과가 없으면 true 반환
            if (!rs.next() || rs.getInt(1) == 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }

        return false;
    }


    public static String getCustomName() {
        return custom_name;
    }
    public static boolean isLarge() {
        return is_large;
    }


    public static int create_new_pizza_in_database() {
        Connection conn = null;
        CallableStatement pstmt = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";
        Integer new_pizza_id = -1;

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            // 저장 프로시저 호출
            pstmt = conn.prepareCall("{call create_new_pizza(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, CustomPizza.getCustomName());
            pstmt.setString(2, CustomPizza.isLarge() ? "L" : "M");

            double[] topping_infos = CustomPizza.calculate_topping_infos();
            pstmt.setDouble(3, topping_infos[0] + CustomPizza.getBase_infos()[0]);
            pstmt.setDouble(4, topping_infos[1] + CustomPizza.getBase_infos()[1]);
            pstmt.setDouble(5, topping_infos[2] + CustomPizza.getBase_infos()[2]);
            pstmt.setDouble(6, topping_infos[3] + CustomPizza.getBase_infos()[3]);
            pstmt.setDouble(7, topping_infos[4] + CustomPizza.getBase_infos()[4]);
            pstmt.setDouble(8, topping_infos[5] + CustomPizza.getBase_infos()[5]);
            pstmt.setDouble(9, topping_infos[6] + CustomPizza.getPrice());

            pstmt.setString(10, CustomerUser.getId());

            pstmt.registerOutParameter(11, Types.INTEGER);

            pstmt.execute();

            // 반환된 주문 ID 가져오기
            new_pizza_id = pstmt.getInt(11);

            pstmt.close();
            DatabaseConnect.commit(conn);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        return new_pizza_id;
    }

    public static double[] calculate_topping_infos() {
    /*
    0 = weight
    1 = calories
    2 = proteins
    3 = fats
    4 = salts
    5 = sugars
    6 = price
     */
        double[] returnarray = {0, 0, 0, 0, 0, 0, 0};

        Set<String> toppingnames = CustomPizza.getToppings().keySet();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "SELECT weight, calories, proteins, fats, salts, sugars, price FROM ingredient WHERE name = ?";
            pstmt = conn.prepareStatement(sql);

            for (String name : toppingnames) {
                pstmt.setString(1, name);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    returnarray[0] += rs.getDouble("weight");
                    returnarray[1] += rs.getDouble("calories");
                    returnarray[2] += rs.getDouble("proteins");
                    returnarray[3] += rs.getDouble("fats");
                    returnarray[4] += rs.getDouble("salts");
                    returnarray[5] += rs.getDouble("sugars");
                    returnarray[6] += rs.getDouble("price");
                }
                rs.close();
            }
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }

        return returnarray;
    }

    public static double[] calculate_base_infos(){
        /*
    0 = weight
    1 = calories
    2 = proteins
    3 = fats
    4 = salts
    5 = sugars
    6 = price
     */
        double[] returnarray = {0, 0, 0, 0, 0, 0, 0};

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "SELECT weight, calories, proteins, fats, salts, sugars, price FROM ingredient WHERE name = ?";
            pstmt = conn.prepareStatement(sql);

            for (String name : CustomPizza.getBase()) {
                pstmt.setString(1, name);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    returnarray[0] += rs.getDouble("weight");
                    returnarray[1] += rs.getDouble("calories");
                    returnarray[2] += rs.getDouble("proteins");
                    returnarray[3] += rs.getDouble("fats");
                    returnarray[4] += rs.getDouble("salts");
                    returnarray[5] += rs.getDouble("sugars");
                    returnarray[6] += rs.getDouble("price");
                }
                rs.close();
            }
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }

        return returnarray;
    }


    public static double getPrice() {
        return price;
    }

    public static double[] getBase_infos(){
        return base_infos;
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
                String path = folderPath + topping_names[i-1] + ".png";
                System.out.println(path);
                File tmp = new File(path);
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
            String outputPath = System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Custom_Pizzas/"+customPizzaName_customerId+".png";
            ImageIO.write(result, "png", new File(outputPath));
            System.out.println("이미지 생성이 완료되었습니다. (" + outputPath + ")");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
