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

    public static void setCustomName(String custom_name) {
        CustomPizza.custom_name = custom_name;
        System.out.println(CustomPizza.getCustomName());
    }

    public static String getCustomName() {
        return custom_name;
    }
    public static boolean isLarge() {
        return is_large;
    }
}
