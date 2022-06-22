package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name = "name",length = 200)
    private String name;

    @Column(name = "description" ,columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "stars")
    private int stars;

    @Column(name = "smallPicURL")
    private String smallPicURL;

    @Column(name = "largePicURL")
    private String largePicURL;

    @Column(name = "addedDate")
    private Date addedDate;

    @Column(name = "inTopPage")
    private boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brands brands;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Categories> categories;

}
