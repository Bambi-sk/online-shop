package com.example.demo.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private  Long id;
    private String name;
    private String description;
    private  int price;
    private int amount;
    private int stars;
    private String pictureUrl;

}
