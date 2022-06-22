package com.example.demo.db;

import java.util.ArrayList;

public class DBManager {
    private  static ArrayList<Item> items=new ArrayList<>();
    static {
        Item item=new Item(1L,"IPHONE 12 PRO MAX","Apple iPhone 12 Pro Max has 6.7 inches (17.02 cm) display, 12 MP + 12 MP + 12 MP camera, battery.",2400,40,4,"https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero?wid=926&amp;hei=1112&amp;fmt=jpeg&amp;qlt=80&amp;op_usm=0.5,0.5&amp;.v=1602088412000");
        items.add(item);
    }
    public static ArrayList<Item> getItems(){
        return items;
    }
    private static Long id=2L;
    public  static void addItem(Item item){
        item.setId(id);
        items.add(item);
        id++;
    }
    public static Item getTask(Long id){
        for(Item t:items){
            if(t.getId()==id){
                return t;
            }
        }
        return null;
    }
}
