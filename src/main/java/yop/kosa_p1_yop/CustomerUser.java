package yop.kosa_p1_yop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class CustomerUser {
    static String name = null;
    static double credits;
    private static String id = null;
    private static String pwd = null;
    private static Map<String, Map<Integer, Integer>> bucket;
    private static Map<String, Map<Integer, Integer>> order;
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
        CustomerUser.order = new HashMap<>();
        CustomerUser.history = new HashMap<>();
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

    public static void setBucket(String selectedName, int selectedId) {
        if (bucket.get(selectedName) == null) {
            Map<Integer, Integer> countTypes = new HashMap<>();
            countTypes.put(selectedId, 1);
            bucket.put(selectedName, countTypes);
        } else {
            if (bucket.get(selectedName).get(selectedId) != null && bucket.get(selectedName).get(selectedId) >= 1) {
                int updatedAmount = bucket.get(selectedName).get(selectedId) + 1;
                bucket.get(selectedName).put(selectedId, updatedAmount);
            } else {
                System.out.println("Something went wrong");
            }
        }
    }

    public static Map<String, Map<Integer, Integer>> getBucket() {
        return bucket;
    }

    /*
     * After User Checkout the bucket selections, existing bucket will be empty
     * and order list will be created
     */
    public static Map<String, Map<Integer, Integer>> checkoutBucket() {
        order = new HashMap<>();
        order.putAll(bucket);
        bucket.clear();
        return order;
    }

    public static void logout() {
        id = null;
        pwd = null;
        name = null;
        credits = 0.00;
        bucket.clear();
        order.clear();
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

    public static void changepwd(String id, String old_pwd, String new_pwd) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "UPDATE customer SET pwd = '" + new_pwd + "' WHERE id = '" + id + "' AND pwd = '" + old_pwd + "'";


            rs = DatabaseConnect.getSQLResult(conn, sql);

            DatabaseConnect.closeResultSet(rs);

            DatabaseConnect.commit(conn);

            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }

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
