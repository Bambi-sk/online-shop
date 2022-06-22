package com.example.demo.services.impl;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PicturesRepository picturesRepository;

    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private BrandsRepository brandsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CommentsRepository commentsRepository;


    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Items> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Items getItem(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public void deleteItem(Items item) {
        itemRepository.delete(item);
    }

    @Override
    public Items saveItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Items> getByTop() {
        return itemRepository.findAllByInTopPageIsTrue();
    }

    @Override
    public List<Items> getByName(String name) {
        name="%"+name+"%";
        return itemRepository.findAllByNameLike(name);
    }

    @Override
    public List<Items> findAllByNameLikeOrderByPriceAsc(String name) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeOrderByPriceAsc(name);
    }

    @Override
    public List<Items> findAllByNameLikeOrderByPriceDesc(String name) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeOrderByPriceDesc(name);
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeAndPriceBetweenOrderByPriceAsc(name , price1 , price2);
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeAndPriceBetweenOrderByPriceDesc(name , price1 , price2);
    }

    @Override
    public List<Items> findAllByOrderByPriceDesc() {
        return itemRepository.findAllByOrderByPriceDesc();
    }

    @Override
    public List<Items> findAllByOrderByPriceAsc() {
        return itemRepository.findAllByOrderByPriceAsc();
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceGreaterThanOrderByPriceAsc(String name, double from) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeAndPriceGreaterThanOrderByPriceAsc(name , from);
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceGreaterThanOrderByPriceDesc(String name, double to) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeAndPriceGreaterThanOrderByPriceDesc(name , to);
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceLessThanOrderByPriceAsc(String name, double from) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeAndPriceLessThanOrderByPriceAsc(name , from);
    }

    @Override
    public List<Items> findAllByNameLikeAndPriceLessThanOrderByPriceDesc(String name, double from) {
        name = "%" + name + "%";
        return itemRepository.findAllByNameLikeAndPriceLessThanOrderByPriceDesc(name , from);
    }

    @Override
    public List<Items> findAllByPriceGreaterThanOrderByPriceAsc(double from) {
        return itemRepository.findAllByPriceGreaterThanOrderByPriceAsc(from);
    }

    @Override
    public List<Items> findAllByPriceGreaterThanOrderByPriceDesc(double from) {
        return itemRepository.findAllByPriceGreaterThanOrderByPriceDesc(from);
    }

    @Override
    public List<Items> findAllByPriceLessThanOrderByPriceAsc(double to) {
        return itemRepository.findAllByPriceLessThanOrderByPriceAsc(to);
    }

    @Override
    public List<Items> findAllByPriceLessThanOrderByPriceDesc(double to) {
        return itemRepository.findAllByPriceLessThanOrderByPriceDesc(to);
    }

    @Override
    public List<Items> findAllByPriceBetweenOrderByPriceAsc(double price1, double price2) {
        return itemRepository.findAllByPriceBetweenOrderByPriceAsc(price1, price2);
    }

    @Override
    public List<Items> findAllByCategoriesIdOrderByPriceAsc(Long id) {
        return itemRepository.findAllByCategoriesIdOrderByPriceAsc(id);
    }

    @Override
    public List<Items> findAllByPriceBetweenOrderByPriceDesc(double price1, double price2) {
        return itemRepository.findAllByPriceBetweenOrderByPriceDesc(price1, price2);
    }

    @Override
    public List<Countries> getAllCountry() {
        return countriesRepository.findAll();
    }

    @Override
    public Countries addCountry(Countries country) {
        return countriesRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countriesRepository.save(country);
    }

    @Override
    public Countries getCountry(Long id) {
        return countriesRepository.getOne(id);
    }

    @Override
    public void deleteCountry(Countries country) {
            countriesRepository.delete(country);
    }

    @Override
    public List<Brands> getAllBrands() {
        return brandsRepository.findAll();
    }

    @Override
    public Brands addBrand(Brands brand) {
        return brandsRepository.save(brand);
    }

    @Override
    public Brands saveBrand(Brands brand) {
        return brandsRepository.save(brand);
    }

    @Override
    public Brands getBrand(Long id) {
        return brandsRepository.getOne(id);
    }

    @Override
    public void deleteBrand(Brands brand) {
        brandsRepository.delete(brand);
    }

    @Override
    public List<Items> getItemsByBrand(Long id) {
        return itemRepository.findAllByBrandsId(id);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoriesRepository.save(category);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoriesRepository.save(category);
    }

    @Override
    public Categories getCategory(Long id) {
        return categoriesRepository.getOne(id);
    }

    @Override
    public void deleteCategory(Categories category) {
            categoriesRepository.delete(category);
    }

    @Override
    public List<Items> getAllByCategories(Long id) {
        return itemRepository.findAllByCategoriesId(id);
    }

    @Override
    public List<Pictures> getAllPictures() {
        return picturesRepository.findAll();
    }

    @Override
    public Pictures addPicture(Pictures picture) {
        return picturesRepository.save(picture);
    }

    @Override
    public Pictures savePicture(Pictures picture) {
        return picturesRepository.save(picture);
    }

    @Override
    public Pictures getPicture(Long id) {
        return picturesRepository.getOne(id);
    }

    @Override
    public void deletePicture(Pictures picture) {
        picturesRepository.delete(picture);
    }

    @Override
    public List<Pictures> getAllPicturesByItemId(Long id) {
        return picturesRepository.getAllPicturesByItemsId(id);
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public Orders addOrder(Orders order) {
        return ordersRepository.save(order);
    }

    @Override
    public Orders saveOrder(Orders order) {
        return ordersRepository.save(order);
    }

    @Override
    public Orders getOrder(Long id) {
        return ordersRepository.getOne(id);
    }

    @Override
    public void deleteOrder(Orders order) {
        ordersRepository.delete(order);
    }

    @Override
    public Comments addComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public List<Comments> gelAllComments() {
        return commentsRepository.findAll();
    }

    @Override
    public Comments getComment(Long id) {
        return commentsRepository.getOne(id);
    }

    @Override
    public void deleteComment(Comments comment) {
            commentsRepository.delete(comment);
    }

    @Override
    public Comments saveComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public List<Comments> getAllCommentsByItem(Long id) {
        return commentsRepository.findAllByItemsId(id);
    }
}
