package yop.kosa_p1_yop;

import java.util.*;

public class CustomerUser {
    String id = null;
    Map<String, Map<Integer,Integer>> bucket;
    Map<String, Map<Integer,Integer>> order;


    public CustomerUser(String id, String name){
        this.id = id;
        this.bucket = new HashMap<>();
    }
    public String getId(){
        return this.id;
    }

    public void setBucket(String selectedName, int selectedId){
        if (this.bucket.get(selectedName) == null) {
            Map<Integer, Integer> countTypes = new HashMap<>();
            countTypes.put(selectedId, 1);
            this.bucket.put(selectedName, countTypes);
        } else {
            if (this.bucket.get(selectedName).get(selectedId) >= 1){
                int updated_amount = this.bucket.get(selectedName).get(selectedId) + 1;
                this.bucket.get(selectedName).put(selectedId, updated_amount);
            } else {
                System.out.println("Something went wrong");
            }
        }
    }

    public Map<String, Map<Integer,Integer>> getBucket(){
        return this.bucket;
    }
    /*
    After User Checkout the bucket selections, existing bucket will be empty and order list will be created
     */

    public Map<String, Map<Integer,Integer>> checkoutBucket(){
        this.order = new HashMap<>();
        this.order.putAll(this.bucket);
        this.bucket = null;
        return this.order;
    }

}
