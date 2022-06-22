package com.example.demo.services;

import com.example.demo.entities.*;

import java.util.List;

public interface ItemService {
    Items addItem(Items item);
    List<Items> getAllItems();
    Items getItem(Long id);
    void deleteItem(Items item);
    Items saveItem(Items item);
    List<Items> getByTop();
    List<Items> getByName(String name);
    List<Items> findAllByNameLikeOrderByPriceAsc(String name);
    List<Items> findAllByNameLikeOrderByPriceDesc(String name);
    List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2);
    List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2);
    List<Items> findAllByOrderByPriceDesc();
    List<Items> findAllByOrderByPriceAsc();
    List<Items> findAllByNameLikeAndPriceGreaterThanOrderByPriceAsc(String name , double from);
    List<Items> findAllByNameLikeAndPriceGreaterThanOrderByPriceDesc(String name , double from);
    List<Items> findAllByNameLikeAndPriceLessThanOrderByPriceAsc(String name, double from);
    List<Items> findAllByNameLikeAndPriceLessThanOrderByPriceDesc(String name, double from);
    List<Items> findAllByPriceGreaterThanOrderByPriceAsc(double from);
    List<Items> findAllByPriceGreaterThanOrderByPriceDesc(double from);
    List<Items> findAllByPriceLessThanOrderByPriceAsc(double to);
    List<Items> findAllByPriceLessThanOrderByPriceDesc(double to);
    List<Items> findAllByPriceBetweenOrderByPriceAsc(double price1, double price2);
    List<Items> findAllByCategoriesIdOrderByPriceAsc(Long id);
    List<Items> findAllByPriceBetweenOrderByPriceDesc(double price1, double price2);

    List<Countries> getAllCountry();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);
    void deleteCountry(Countries country);

    List<Brands> getAllBrands();
    Brands addBrand(Brands brand);
    Brands saveBrand(Brands brand);
    Brands getBrand(Long id);
    void deleteBrand(Brands brand);
    List<Items> getItemsByBrand(Long id );

    List<Categories> getAllCategories();
    Categories addCategory(Categories category);
    Categories saveCategory(Categories category);
    Categories getCategory(Long id);
    void deleteCategory(Categories category);
    List<Items> getAllByCategories(Long id);

    List<Pictures> getAllPictures();
    Pictures addPicture(Pictures picture);
    Pictures savePicture(Pictures picture);
    Pictures getPicture(Long id);
    void deletePicture(Pictures picture);
    List<Pictures> getAllPicturesByItemId(Long id);


    List<Orders> getAllOrders();
    Orders addOrder(Orders order);
    Orders saveOrder(Orders order);
    Orders getOrder(Long id);
    void deleteOrder(Orders order);

    Comments addComment(Comments comment);
    List<Comments> gelAllComments();
    Comments getComment(Long id);
    void deleteComment(Comments comment);
    Comments saveComment(Comments comment);
    List<Comments> getAllCommentsByItem(Long id);

}
