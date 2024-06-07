package yop.kosa_p1_yop;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;


public class CustomerUser {
    static String name = null;
    static double credits;
    private static String id = null;
    private static String pwd = null;

    private static Map<String, Map<Integer, Integer>> bucket;
    private static double bucket_price;

    private static Map<String, Map<Integer, Integer>> current_order;




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
            CustomerUser.update_bucket_price(selectedType, selectedId, true);
            bucket.put(selectedType, countTypes);
            return true;
        } else {
            if (bucket.get(selectedType).get(selectedId) != null && bucket.get(selectedType).get(selectedId) >= 1) {
                int updatedAmount = bucket.get(selectedType).get(selectedId) + 1;
                bucket.get(selectedType).put(selectedId, updatedAmount);
                CustomerUser.update_bucket_price(selectedType, selectedId, true);
                return true;
            } else {
                return false;
            }
        }
    }
    /*
    delete a bucket item from the controller to Customer Account
    String selectedType = pizza / option
    int selectedId = pizza_id / option_id

    Controller에서 option이 남아있는데 마지막 pizza의 갯수를 0으로 내리면 bucket을 없애게 해야 함. => CustomerUser.bucket.clear();
     */
    public static boolean delete_from_bucket(String selectedType, int selectedId){
        if (bucket.get(selectedType).get(selectedId) != null){
            if (bucket.get(selectedType).get(selectedId) == 1){
                bucket.get(selectedType).remove(selectedId);
                if(bucket.get(selectedType).isEmpty()){
                    bucket.remove(selectedType);
                }
            } else {
                bucket.get(selectedType).put(selectedId, bucket.get(selectedType).get(selectedId)-1);
            }
            CustomerUser.update_bucket_price(selectedType, selectedId, false);
            return true;
        } else {
            System.out.println("Error Occured while delete from bucket.");
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
                if(selectedType.equals("pizza")) {
                    sql = "SELECT price FROM pizza WHERE id = '"+selectedId+"'";
                } else if(selectedType.equals("option")){
                    sql = "SELECT price FROM options WHERE id = '"+selectedId+"'";
                }
                rs = DatabaseConnect.getSQLResult(conn, sql);
                if(rs.next()){
                    double price = rs.getDouble("price");
                    CustomerUser.bucket_price = CustomerUser.get_bucket_price() + price;
                }
            } else {
                if(selectedType.equals("pizza")) {
                    sql = "SELECT price FROM pizza WHERE id = '"+selectedId+"'";
                } else if(selectedType.equals("option")){
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

    public static boolean payToCheckout_card(String cardNumber, int cvc, int pwd){
        /*
        json 으로 카드 정보 가져와서 카드로 계산하는 경우.
         */
        try {
            boolean cardFound = true;
//            String filePath = "src/main/resources/cards.json"; // 파일 경로
//            String content = new String(Files.readAllBytes(Paths.get(filePath)));
//            JSONArray cardsArray = new JSONArray(content);
//
//            boolean cardFound = false;
//
//            for (int i = 0; i < cardsArray.length(); i++) {
//                JSONObject card = cardsArray.getJSONObject(i);
//                String storedCardNumber = card.getString("number");
//                int storedCvc = card.getInt("cvc");
//                int storedPwd = card.getInt("pwd");
//
//                if (storedCardNumber.equals(cardNumber) && storedCvc == cvc && storedPwd == pwd) {
//                    cardFound = true;
//                    break;
//                }
//            }

            if (cardFound) {
                System.out.println("결제가 성공적으로 처리되었습니다.");
                double updated_credits = updateCredits(true);
                if (updated_credits > CustomerUser.getCredits()) {
                    CustomerUser.credits = updated_credits;

                    boolean db_insert = checkoutBucket();
                    if (!db_insert) {
                        System.out.println("Cannot checkout to bucket.");
                        return false;
                    }
                    return true;
                }
            } else {
                System.out.println("카드 정보를 확인할 수 없습니다.");
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
        if (updated_credits >= 0) {
            CustomerUser.credits = updated_credits;

            boolean db_insert = checkoutBucket();
            if (!db_insert) {
                System.out.println("Cannot checkout to bucket.");
                return false;
            }
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
                String sql = "UPDATE customers SET credits = " + update_amount + "WHERE id = '" + CustomerUser.getId() + "'";
                rs = DatabaseConnect.getSQLResult(conn, sql);
                DatabaseConnect.commit(conn);
                DatabaseConnect.closeResultSet(rs);
                return update_amount;

            } else {
                double update_amount = CustomerUser.getCredits() - CustomerUser.get_bucket_price();
                if(update_amount < 0) return -1;
                String sql = "UPDATE customers SET credits = " + update_amount + "WHERE id = '" + CustomerUser.getId() + "'";
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
                if (key.equals("pizza")) {
                    Set<Integer> pizza_ids = bucket.get("pizza").keySet();
                    for (Integer pid : pizza_ids){
                        boolean result = create_new_order_item_in_database(new_order_id, pid, 0);
                        if (!result){
                            System.out.println("Can not insert order item:\t" + pid);
                            return false;
                        }
                    }

                } else if (key.equals("option")) {
                    Set<Integer> option_ids = bucket.get("option").keySet();
                    for (Integer oid : option_ids){
                        boolean result = create_new_order_item_in_database(new_order_id, 0, oid);
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
            return true;

        } else {
            System.out.println("Error occured");
        }

        bucket.clear();
        return false;
    }

    private static boolean create_new_order_item_in_database(int new_order_id, int pizza_id, int options_id) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);
            System.out.println("Before");

            String sql2 = "INSERT INTO orders_item(orders_id, pizza_id, options_id) VALUES ("+ new_order_id +","+ pizza_id +","+ options_id +")";
            rs = DatabaseConnect.getSQLResult(conn, sql2);
            DatabaseConnect.closeResultSet(rs);
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

    public static Integer create_new_order_in_database(){
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);
            System.out.println("Before");
            String sql1 = "SELECT orders_seq.NEXTVAL FROM dual;";
            Integer new_order_id = 0;
            rs = DatabaseConnect.getSQLResult(conn, sql1);
            if (rs != null)  {
                new_order_id = rs.getInt(1);
            } else {
                return -1;
            }
            DatabaseConnect.closeResultSet(rs);

            if(new_order_id > 0){
                String sql2 = "INSERT INTO orders(customer_id, price) VALUES ("+CustomerUser.getId()+","+CustomerUser.get_bucket_price()+")";
                rs = DatabaseConnect.getSQLResult(conn, sql2);
                DatabaseConnect.closeResultSet(rs);
                DatabaseConnect.commit(conn);
            }

            DatabaseConnect.closeConnection(conn);

            return new_order_id;

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        return -1;

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



    public static void setCurrentOrder(){

    }

    public static Map<String, Map<String, Map<Integer, Integer>>> getCurrentOrder(){
        return getCurrentOrder();
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
            String sql1 = "select * from orders o, orders_item oi where o.id = oi.orders_id and o.customer_id = '" + CustomerUser.getId() + "'";
            rs = DatabaseConnect.getSQLResult(conn, sql1);

            while (rs.next()) {
                Integer order_id = rs.getInt("orders_id");
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
            String sql1 = "SELECT oi.pizza_id, p.name, COUNT(*) AS quantity " +
                    "FROM orders_item oi " +
                    "INNER JOIN pizza p ON oi.pizza_id = p.id " +
                    "WHERE oi.orders_id = '" + order_id + "' " +
                    "GROUP BY oi.pizza_id, p.name";
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
            String sql2 = "SELECT oi.options_id, o.name, COUNT(*) AS quantity " +
                    "FROM orders_item oi " +
                    "INNER JOIN options o ON oi.options_id = o.id " +
                    "WHERE oi.orders_id = '" + order_id + "' " +
                    "GROUP BY oi.options_id, o.name";
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

}
