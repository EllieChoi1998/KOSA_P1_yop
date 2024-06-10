package yop.kosa_p1_yop;

import java.util.*;

public class CustomPizza {
    private static String custom_name;
    private static String user_id;
    private static String[] base;
    private static Map<String, Boolean> toppings;
    private static boolean is_large;
    private static double price;

    private CustomPizza() {
        // private 생성자로 인스턴스화 방지
    }

    public static void createCustomPizza(String[] base, boolean is_large){
        CustomPizza.base = base;
        CustomPizza.toppings = new HashMap<>();
        CustomPizza.is_large = is_large;

        for (String b : base){
            System.out.println(b);
        }

        System.out.println(base + "\t" + toppings + "\t" + is_large + "\t" + price);
    }

    public static boolean addToppings(String toppingName) {
        if(toppings.get(toppingName) == null){
            return false;
        } else {
            toppings.put(toppingName, true);
            return true;
        }
    }

    public static boolean deleteToppings(String toppingName){
        if(toppings.get(toppingName)){
            toppings.remove(toppingName);
            return true;
        } else {
            return false;
        }
    }

    public static String[] getBase(){
        return base;
    }

    public static Map<String, Boolean> getToppings(){
        return toppings;
    }

    public static void resetPizza() {
        base = null;
        toppings = null;
    }
}
