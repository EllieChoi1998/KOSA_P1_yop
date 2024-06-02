package yop.kosa_p1_yop;

import java.util.HashMap;

public class AdminUser {
    private static String id = null;
    private static String pwd = null;
    static String name = null;
    static String myrole = null;

    // Private constructor to prevent instantiation
    private AdminUser() {
    }

    public static void initialize(String id, String pwd, String name) {
        AdminUser.id = id;
        AdminUser.pwd = pwd;
        AdminUser.name = name;
        AdminUser.myrole = "Admin";
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

    public static String getmyrole(){
        return myrole;
    }
}
