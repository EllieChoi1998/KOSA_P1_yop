package yop.kosa_p1_yop;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.io.*;


public class CustomerUser {
    static String name = null;
    static double credits;
    private static String id = null;
    private static String pwd = null;

    private static Map<String, Map<Integer, Integer>> bucket;
    private static double bucket_price;

    private static Map<String, Map<Integer, Integer>> current_order = new HashMap<>();
    private static Integer current_order_id;
    private static String current_order_status;
    private static boolean complete_current_order;
    private static double current_order_price;


    private static Map<Integer, List<String>> history;

    // Private constructor to prevent instantiation
    private CustomerUser() {
    }

    public static void initialize(String id, String pwd, String name, double credits) {
        CustomerUser.id = id;
        CustomerUser.pwd = pwd;
        CustomerUser.name = name;
        CustomerUser.credits = credits;
        CustomerUser.bucket = new HashMap<>();
        CustomerUser.history = new HashMap<>();
        CustomerUser.bucket_price = 0;

        setCurrentOrder();
    }



    public static String getId() {
        return id;
    }

    public static String getPwd() {
        return pwd;
    }

    public static String getName() {
        return name;
    }

    public static double getCredits() {
        return credits;
    }

    public static double get_bucket_price(){
        return bucket_price;
    }

    /*
    set a bucket from the controller to Customer Account
    String selectedType = Pizza / Option
    int selectedId = pizza_id / option_id

    Controller에서 pizza 없을땐 option 선택 불가하게 해야 함.
     */
    public static boolean add_to_bucket(String selectedType, int selectedId) {
        if (bucket.get(selectedType) == null) {
            Map<Integer, Integer> countTypes = new HashMap<>();
            countTypes.put(selectedId, 1);
            bucket.put(selectedType, countTypes);
        } else {
            bucket.get(selectedType).put(selectedId, bucket.get(selectedType).getOrDefault(selectedId, 0) + 1);
        }
        CustomerUser.update_bucket_price(selectedType, selectedId, true);
        return true;
    }

    public static boolean delete_from_bucket(String selectedType, int selectedId) {
        if (bucket.get(selectedType) != null && bucket.get(selectedType).get(selectedId) != null) {
            int currentQuantity = bucket.get(selectedType).get(selectedId);
            if (currentQuantity == 1) {
                bucket.get(selectedType).remove(selectedId);
                if (bucket.get(selectedType).isEmpty()) {
                    bucket.remove(selectedType);
                }
            } else {
                bucket.get(selectedType).put(selectedId, currentQuantity - 1);
            }
            CustomerUser.update_bucket_price(selectedType, selectedId, false);
            return true;
        } else {
            System.out.println("Error Occurred while deleting from bucket.");
            return false;
        }
    }


    public static void update_bucket_price(String selectedType, int selectedId, boolean increase){
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);
            String sql = "";
            if (increase){
                if(selectedType.equals("Pizza")) {
                    sql = "SELECT price FROM pizza WHERE id = '"+selectedId+"'";
                } else if(selectedType.equals("Option")){
                    sql = "SELECT price FROM options WHERE id = '"+selectedId+"'";
                }
                rs = DatabaseConnect.getSQLResult(conn, sql);
                if(rs.next()){
                    double price = rs.getDouble("price");
                    CustomerUser.bucket_price = CustomerUser.get_bucket_price() + price;
                }
            } else {
                if(selectedType.equals("Pizza")) {
                    sql = "SELECT price FROM pizza WHERE id = '"+selectedId+"'";
                } else if(selectedType.equals("Option")){
                    sql = "SELECT price FROM options WHERE id = '"+selectedId+"'";
                }
                rs = DatabaseConnect.getSQLResult(conn, sql);
                if(rs.next()){
                    double price = rs.getDouble("price");
                    CustomerUser.bucket_price = CustomerUser.get_bucket_price() - price;
                }
            }


            DatabaseConnect.closeResultSet(rs);

            DatabaseConnect.commit(conn);

            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }

    public static Map<String, Map<Integer, Integer>> getBucket() {
        return bucket;
    }

    public static boolean payToCheckout_card(String cardNumber, String cvc, String pwd) throws Exception{
        /*
        csv 에서 카드 정보 가져와서 카드로 계산하는 경우.
         */
        System.out.println("CURRENT CARD: " + cardNumber);
        System.out.println("CURRENT CVC: " + cvc);
        System.out.println("CURRENT pwd: " + pwd);
        // 입력 스트림 오브젝트 생성
        BufferedReader br = null;
        try {

            boolean cardNotFound = true;
            boolean cardFound = false;

            // 대상 CSV 파일의 경로 설정
            br = Files.newBufferedReader(Paths.get(System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/cards.csv"), Charset.forName("UTF-8"));
            // CSV파일에서 읽어들인 1행분의 데이터
            String line = "";

            while((line = br.readLine()) != null && cardNotFound) {
                // CSV 파일의 1행을 저장하는 리스트 변수
                List<String> tmpList = new ArrayList<>();
                String array[] = line.split(",");
                String f_cardNumber = array[0];
                String f_cvc = array[1];
                String f_pwd = array[2];
                if(f_cardNumber.trim().equals(cardNumber) && f_cvc.trim().equals(cvc) && f_pwd.trim().equals(pwd)) {
                    // 리스트 내용 출력
                    System.out.println("f_cardNumber: " + f_cardNumber + "\nf_cvc: " + f_cvc + "\nf_pwd: " + f_pwd);
                    cardFound = true;
                    cardNotFound = false;
                    System.out.println();
                }
            }

            if (cardFound) {
                System.out.println("Card Payment success");
                double updated_credits = updateCredits(true);
                if (updated_credits > CustomerUser.getCredits()) {
                    CustomerUser.credits = updated_credits;


                    boolean db_insert = checkoutBucket();
                    if (!db_insert) {
                        System.out.println("Cannot checkout to bucket.");
                        return false;
                    }

                    CustomerUser.setCurrentOrder();
                    return true;
                }
            } else {
                System.out.println("cannot process card payment");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean payToCheckout_rewards(){
        /*
        rewards point로 계산하는 경우.
         */
        double updated_credits = updateCredits(false);
        System.out.println("Updated credits amount: " + updated_credits);
        if (updated_credits >= 0) {
            CustomerUser.credits = updated_credits;

            boolean db_insert = checkoutBucket();
            if (!db_insert) {
                System.out.println("Cannot checkout to bucket.");
                return false;
            }

            CustomerUser.setCurrentOrder();

            return true;
        }
        return false;
    }

    private static double updateCredits(boolean is_card) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            if(is_card){
                double update_amount = CustomerUser.getCredits() + CustomerUser.get_bucket_price() * 0.1;
                String sql = "UPDATE customer SET credits = " + (int) update_amount + "WHERE id = '" + CustomerUser.getId() + "'";
                rs = DatabaseConnect.getSQLResult(conn, sql);
                DatabaseConnect.commit(conn);
                DatabaseConnect.closeResultSet(rs);
                return update_amount;

            } else {
                System.out.println("CustomerCredits: " + CustomerUser.getCredits());
                System.out.println("Customer bucket price: " + CustomerUser.get_bucket_price());
                double update_amount = CustomerUser.getCredits() - CustomerUser.get_bucket_price();
                System.out.println("Upadated : " + update_amount);
                if(update_amount < 0) return -1;
                String sql = "UPDATE customer SET credits = " + (int) update_amount + "WHERE id = '" + CustomerUser.getId() + "'";
                rs = DatabaseConnect.getSQLResult(conn, sql);
                DatabaseConnect.commit(conn);
                DatabaseConnect.closeResultSet(rs);
                return update_amount;
            }

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        return -1;
    }


    /*
     * After User Checkout the bucket selections, existing bucket will be empty
     * and order list will be created
     */
    public static boolean checkoutBucket() {
        int new_order_id = create_new_order_in_database();
        if (new_order_id != -1) {
            Set<String> bucket_keys = bucket.keySet();
            for (String key : bucket_keys) {
                if (key.equals("Pizza")) {
                    Set<Integer> pizza_ids = bucket.get("Pizza").keySet();
                    for (Integer pid : pizza_ids){
                        boolean result = create_new_order_item_in_database(new_order_id, 1, pid, CustomerUser.getBucket().get("Pizza").get(pid));
                        if (!result) {
                            System.out.println("Can not insert order item:\t" + pid);
                            return false;
                        }
                    }

                } else if (key.equals("Option")) {
                    Set<Integer> option_ids = bucket.get("Option").keySet();
                    for (Integer oid : option_ids){

                        boolean result = create_new_order_item_in_database(new_order_id, 2, oid, CustomerUser.getBucket().get("Option").get(oid));
                        if (!result) {
                            System.out.println("Can not insert order item:\t" + oid);
                            return false;

                        }
                    }
                } else {
                    System.out.println("Invalid type has been given. Cannot invoke to Customer User's order List.");
                    return false;
                }
            }
            bucket.clear();
            bucket_price = 0;
            return true;

        } else {
            System.out.println("Error occured");
        }
        bucket.clear();
        return false;
    }

    public static Integer create_new_order_in_database() {
        Connection conn = null;
        CallableStatement pstmt = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";
        Integer new_order_id = -1;

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            // 저장 프로시저 호출
            pstmt = conn.prepareCall("{call create_new_order(?, ?, ?, ?)}");
            pstmt.setString(1, CustomerUser.getId());
            pstmt.setDouble(2, CustomerUser.get_bucket_price());
            pstmt.setInt(3, 1);
            pstmt.registerOutParameter(4, Types.INTEGER);

            pstmt.execute();

            // 반환된 주문 ID 가져오기
            new_order_id = pstmt.getInt(4);

            pstmt.close();
            DatabaseConnect.commit(conn);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        return new_order_id;
    }



    private static boolean create_new_order_item_in_database(int new_order_id, int type, int type_id, int quantity) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);
            System.out.println("Before");
            if(type == 1) {
                String sql2 = "INSERT INTO orders_pizza(orders_id, pizza_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql2);
                pstmt.setInt(1, new_order_id);
                pstmt.setInt(2, type_id);
                pstmt.setInt(3, quantity);
                pstmt.executeUpdate();
                pstmt.close();
            } else if(type == 2){
                String sql2 = "INSERT INTO orders_options(orders_id, options_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql2);
                pstmt.setInt(1, new_order_id);
                pstmt.setInt(2, type_id);
                pstmt.setInt(3, quantity);
                DatabaseConnect.commit(conn);
                pstmt.executeUpdate();
                pstmt.close();
            }
            DatabaseConnect.commit(conn);
            DatabaseConnect.closeConnection(conn);

            return true;
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        return false;
    }



    public static void logout() {
        id = null;
        pwd = null;
        name = null;
        credits = 0.00;
        bucket.clear();
        System.out.println("Log Out");
    }

    public static void signout() {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);
            System.out.println("Before");
            String sql = "DELETE FROM customer WHERE id = '" + id + "' AND pwd = '" + pwd + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            System.out.println("AFTER");
            DatabaseConnect.closeResultSet(rs);

            DatabaseConnect.commit(conn);

            DatabaseConnect.closeConnection(conn);

            System.out.println("Sign out. Can not use this user infos anymore");

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }

        logout();
    }

    public static void changepwdOrname(String id, String old_pwd, String new_change, int type) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);
            if (type == 1) {
                String sql = "UPDATE customer SET pwd = '" + new_change + "' WHERE id = '" + id + "' AND pwd = '" + old_pwd + "'";

                rs = DatabaseConnect.getSQLResult(conn, sql);

                DatabaseConnect.closeResultSet(rs);

                DatabaseConnect.commit(conn);
            } else if (type == 2) {
                String sql = "UPDATE customer SET name = '" + new_change + "' WHERE id = '" + id + "'";

                rs = DatabaseConnect.getSQLResult(conn, sql);

                DatabaseConnect.closeResultSet(rs);

                DatabaseConnect.commit(conn);

                CustomerUser.initialize(CustomerUser.getId(), CustomerUser.getPwd(), new_change, CustomerUser.getCredits());
            }

            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }

    }



    public static void setCurrentOrder() {
        Connection conn = null;
        ResultSet rs1 = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            // Get all orders for specific customer user by its id
            String sql1 = "select * from orders where customer_id = '" + CustomerUser.getId() + "'";
            rs1 = DatabaseConnect.getSQLResult(conn, sql1);

            while (rs1.next()) {
                Integer order_status = rs1.getInt("status");
                if (order_status != 0 && order_status != 3 && order_status != null) {
                    current_order_id = rs1.getInt("id");
                    if (order_status == 1) {
                        current_order_status = "Order Submitted";
                    } else if (order_status == 2) {
                        current_order_status = "Order On Delivery";
                    }

                    current_order_price = rs1.getInt("price");

                    Map<Integer, Integer> pizzas = new HashMap<>();
                    Map<Integer, Integer> options = new HashMap<>();
                    String sql2 = "select pizza_id, quantity from orders_pizza where orders_id = " + current_order_id;
                    try (ResultSet rs2 = DatabaseConnect.getSQLResult(conn, sql2)) {
                        while (rs2.next()) {
                            Integer pizza_id = rs2.getInt("pizza_id");
                            Integer quantity = rs2.getInt("quantity");

                            pizzas.put(pizza_id, quantity);

                        }
                    }
                    String sql3 = "select options_id, quantity from orders_options where orders_id = " + current_order_id;
                    try (ResultSet rs3 = DatabaseConnect.getSQLResult(conn, sql3)) {
                        while (rs3.next()) {
                            Integer options_id = rs3.getInt("options_id");
                            Integer quantity = rs3.getInt("quantity");
                            options.put(options_id, quantity);

                        }
                    }
                    if (!pizzas.isEmpty()) current_order.put("Pizza", pizzas);
                    if (!options.isEmpty()) current_order.put("Option", options);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        } finally {
            DatabaseConnect.closeResultSet(rs1);
            DatabaseConnect.closeConnection(conn);
        }
    }

    public static Map<String, Map<Integer, Integer>> getCurrentOrder(){
        return current_order;
    }

    public static void setHistoryOrders() {
        CustomerUser.history.clear();
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            // get all orders for specific customer user by its id
            String sql1 = "select * from orders where customer_id = '" + CustomerUser.getId() + "'";
            rs = DatabaseConnect.getSQLResult(conn, sql1);

            while (rs.next()) {
                Integer order_id = rs.getInt("id");
                Integer order_status = rs.getInt("status");
                if (order_status != 0) {
                    String status = null;
                    if (order_status == 1) status = "[Order Submitted]\t";
                    else if (order_status == 2) status = "[Order On Delivery]\t";
                    else if (order_status == 3) status = "[Order Completed]\t";

                    Integer order_price = rs.getInt("price");
                    String price = "Total Price of this order : " + order_price;

                    List<String> ordered_items = new ArrayList<>();
                    ordered_items.add(status);
                    ordered_items.add(price);

                    history.put(order_id, ordered_items);
                }
            }


            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }
    }

    public static Map<Integer, List<String>> getHistory() {
        CustomerUser.setHistoryOrders();
        return history;
    }

    public static Map<String, Map<Integer, Map<String, Integer>>> getDetailedHistory(int order_id) {
        // initialize detailedhistory hashmap
        Map<String, Map<Integer, Map<String, Integer>>> detailedhistory = new HashMap<>();
        detailedhistory.put("Pizzas", new HashMap<>());
        detailedhistory.put("Options", new HashMap<>());
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            // pizza
            String sql1 = "SELECT op.pizza_id, p.name, op.quantity " +
                    "FROM orders_pizza op INNER JOIN pizza p ON op.pizza_id = p.id " +
                    "WHERE op.orders_id = " + order_id;
            rs = DatabaseConnect.getSQLResult(conn, sql1);

            Map<Integer, Map<String, Integer>> pizzas = new HashMap<>(); // Map to store pizza details

            while (rs.next()) {
                int pizza_id = rs.getInt("pizza_id");
                String pizza_name = rs.getString("name");
                int quantity = rs.getInt("quantity");

                // Check if the pizza_id is already present in the map
                if (pizzas.containsKey(pizza_id)) {
                    // If yes, get the existing map and update the quantity for the pizza_name
                    Map<String, Integer> pizzaDetails = pizzas.get(pizza_id);
                    pizzaDetails.put(pizza_name, quantity);
                } else {
                    // If not, create a new map and put it in the main map
                    Map<String, Integer> pizzaDetails = new HashMap<>();
                    pizzaDetails.put(pizza_name, quantity);
                    pizzas.put(pizza_id, pizzaDetails);
                }
            }

            // Store the pizza details map in the detailed history map
            detailedhistory.put("Pizzas", pizzas);

            DatabaseConnect.closeResultSet(rs);

            // options
            String sql2 = "SELECT oo.options_id, o.name, oo.quantity " +
                    "FROM orders_options oo INNER JOIN options o ON oo.options_id = o.id " +
                    "WHERE oo.orders_id = " + order_id;
            rs = DatabaseConnect.getSQLResult(conn, sql2);

            Map<Integer, Map<String, Integer>> options = new HashMap<>(); // Map to store options details

            while (rs.next()) {
                int option_id = rs.getInt("options_id");
                String option_name = rs.getString("name");
                int quantity = rs.getInt("quantity");

                // Check if the option_id is already present in the map
                if (options.containsKey(option_id)) {
                    // If yes, get the existing map and update the quantity for the option_name
                    Map<String, Integer> optionDetails = options.get(option_id);
                    optionDetails.put(option_name, quantity);
                } else {
                    // If not, create a new map and put it in the main map
                    Map<String, Integer> optionDetails = new HashMap<>();
                    optionDetails.put(option_name, quantity);
                    options.put(option_id, optionDetails);
                }
            }

            // Store the options details map in the detailed history map
            detailedhistory.put("Options", options);

            DatabaseConnect.closeResultSet(rs);


        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }

        return detailedhistory;

    }



    public static double getCurrent_order_price() {
        return current_order_price;
    }

    public static boolean isComplete_current_order() {
        return complete_current_order;
    }

    public static String getCurrent_order_status() {
        return current_order_status;
    }

    public static Integer getCurrent_order_id(){
        return current_order_id;
    }

    public static void completeOrder() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);

            // SQL 쿼리 작성
            String sql = "UPDATE orders SET status = ? WHERE customer_id = ? AND status = ? AND id = ?";
            pstmt = conn.prepareStatement(sql);

            // 파라미터 설정
            pstmt.setInt(1, 3);
            pstmt.setString(2, getId());
            pstmt.setInt(3, 2);
            pstmt.setInt(4, current_order_id);

            // 쿼리 실행
            int rowsAffected = pstmt.executeUpdate();

            // 업데이트된 행의 수 확인
            if (rowsAffected > 0) {
                System.out.println("Order status updated successfully.");
            } else {
                System.out.println("No orders were found with status 'Order On Delivery' for the current customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            try {
                if (pstmt != null) pstmt.close();
                DatabaseConnect.commit(conn);
                DatabaseConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}