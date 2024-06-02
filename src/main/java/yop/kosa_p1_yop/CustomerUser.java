package yop.kosa_p1_yop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class CustomerUser {
    private static String id = null;
    private static String pwd = null;
    static String name = null;
    static double credits;

    private static Map<String, Map<Integer, Integer>> bucket = new HashMap<>();
    private static Map<String, Map<Integer, Integer>> order = new HashMap<>();

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

    public static double getCredits(){
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
            String sql = "DELETE FROM users WHERE id = '"+id+"' AND pwd = '"+pwd+"'";

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

    public static void changepwd(String id, String old_pwd, String new_pwd){
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "UPDATE users SET pwd = '" + new_pwd + "' WHERE id = '" + id + "' AND pwd = '" + old_pwd + "'";


            rs = DatabaseConnect.getSQLResult(conn, sql);

            DatabaseConnect.closeResultSet(rs);

            DatabaseConnect.commit(conn);

            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }

    }
}
