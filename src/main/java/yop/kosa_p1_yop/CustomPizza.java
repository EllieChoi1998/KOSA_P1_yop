package yop.kosa_p1_yop;

import java.util.*;

public class CustomPizza {
    String custom_name;
    String user_id;
    String[] base;
    Map<String, Integer> toppings;

    public CustomPizza(String[] base){
        this.base = base;
        this.toppings = new HashMap<>();
    }

    public boolean addToppings(String toppingName, Integer toppingId) {
        if(this.toppings.get(toppingName) == toppingId){
            return false;
        } else {
            this.toppings.put(toppingName, toppingId);
            return true;
        }
    }

    public boolean deleteToppings(String toppingName, Integer toppingId){
        if(this.toppings.get(toppingName) == toppingId){
            this.toppings.remove(toppingName);
            return true;
        } else {
            return false;
        }
    }

    public String[] getBase(){
        return this.base;
    }

    public Map<String, Integer> getToppings(){
        return this.toppings;
    }

    public void resetPizza() {
        this.base = null;
        this.toppings = null;
    }

}
