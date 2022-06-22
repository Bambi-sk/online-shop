package com.example.demo.controllers;

import com.example.demo.db.DBManager;
import com.example.demo.db.Item;
import com.example.demo.entities.*;
import com.example.demo.services.ItemService;
import com.example.demo.services.UserService;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.extras.springsecurity5.auth.Authorization;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ItemService itemService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.itemsPicture.uploadPath}")
    private String uploadPathPictures;

    @Value("${file.itemsPicture.viewPath}")
    private String viewPathPictures;

    @Value("${file.itemsPicture.defaultPicture}")
    private String defaultPictureItems;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String index(Model model ,HttpSession session){
       List<Items> items=itemService.getByTop();
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        model.addAttribute("items",items);
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("countries",countries);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("currentUser",getUserData());
        return "index";
    }
    @GetMapping(value = "/getAll")
    public String getAll(Model model ,HttpSession session){
        List<Items> items=itemService.getAllItems();
        model.addAttribute("items",items);
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("countries",countries);
        List<Brands> brands=itemService.getAllBrands();
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("brands",brands);
        model.addAttribute("currentUser",getUserData());
        return "index";
    }
    @GetMapping(value = "/addItemlist")
    public String redirectItem(){
        return "addItem";
    }


    @PostMapping(value = "/addItem")
    @PreAuthorize ("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
    public  String addTask(@RequestParam(name = "name",defaultValue = "No task") String name,
                           @RequestParam(name = "description",defaultValue = "No task") String description,
                            @RequestParam(name = "price") double price,
                            @RequestParam(name = "largePicUrl") String largePicUrl,
                           @RequestParam(name = "smallPicUrl") String smallPicUrl,
                           @RequestParam(name ="rating") String rating,
                           @RequestParam(name = "inTop") String inTop,
                           @RequestParam(name = "brandsId" ,defaultValue = "0l") Long brandsId){

        int rating1=0;
        Items items=new Items();
        boolean inTop1=false;
        if(rating.equals("1")){
            rating1=1;
        }
        else if(rating.equals("2")){
            rating1=2;
        }
        else if(rating.equals("3")){
            rating1=3;
        }
        else if(rating.equals("4")){
            rating1=4;
        }
        else if(rating.equals("5")){
            rating1=5;
        }
        if(inTop.equals("YES")){
            inTop1=true;
        }
        else if(inTop.equals("NO")){
            inTop1=false;
        }
        long date=System.currentTimeMillis();
        Date addeddate=new Date(date);

        Brands brand=itemService.getBrand(brandsId);
        if(brand!=null){
            items.setBrands(brand);
            items.setName(name);
            items.setLargePicURL(largePicUrl);
            items.setSmallPicURL(smallPicUrl);
            items.setInTopPage(inTop1);
            items.setStars(rating1);
            items.setAddedDate(addeddate);
            items.setDescription(description);
            items.setPrice(price);
            itemService.addItem(items);
            /*itemService.addItem(new Items(null,name,description,price,rating1,smallPicUrl,largePicUrl,addeddate,inTop1,brand));*/
        }

        return "redirect:/itemsList";
    }



    @PostMapping(value = "/addCountry")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String addCountry(@RequestParam(name = "name",defaultValue = "No task") String name,
                           @RequestParam(name = "code",defaultValue = "No task") String code){




            itemService.addCountry(new Countries(null,name,code));


        return "redirect:/countryList";
    }
    @PostMapping(value = "/addCategory")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String addCategory(@RequestParam(name = "name",defaultValue = "No task") String name,
                              @RequestParam(name = "logoURL",defaultValue = "No task") String logoURL){




        itemService.addCategory(new Categories(null,name,logoURL));


        return "redirect:/categoryList";
    }
    @PostMapping(value = "/addBrand")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String addBrand(@RequestParam(name = "name",defaultValue = "No task") String name,
                              @RequestParam(name = "countryId",defaultValue = "0l") Long countryId){



        Countries country=itemService.getCountry(countryId);
        if(country!=null){
            itemService.addBrand(new Brands(null,name,country));
        }



        return "redirect:/brandsList";
    }
    @PostMapping(value = "/saveItem")
    @PreAuthorize ("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
    public  String saveItem(@RequestParam(name = "name",defaultValue = "No task") String name,
                           @RequestParam(name = "description",defaultValue = "No task") String description,
                           @RequestParam(name = "price") double price,
                           @RequestParam(name = "largePicUrl") String largePicUrl,
                           @RequestParam(name = "smallPicUrl") String smallPicUrl,
                           @RequestParam(name ="rating") String rating,
                           @RequestParam(name = "inTop") String inTop,
                           @RequestParam(name = "id") Long id,
                            @RequestParam(name="brandsId",defaultValue = "Ol") Long brandsId){

        int rating1=0;
        boolean inTop1=false;
        if(rating.equals("1")){
            rating1=1;
        }
        else if(rating.equals("2")){
            rating1=2;
        }
        else if(rating.equals("3")){
            rating1=3;
        }
        else if(rating.equals("4")){
            rating1=4;
        }
        else if(rating.equals("5")){
            rating1=5;
        }
        if(inTop.equals("YES")){
            inTop1=true;
        }
        else if(inTop.equals("NO")){
            inTop1=false;
        }
        Brands brand=itemService.getBrand(brandsId);
        Items items=itemService.getItem(id);
        if(brand!=null) {
            items.setName(name);
            items.setDescription(description);
            items.setPrice(price);
            items.setStars(rating1);
            items.setSmallPicURL(smallPicUrl);
            items.setLargePicURL(largePicUrl);
            items.setInTopPage(inTop1);
            items.setBrands(brand);
            itemService.saveItem(items);
        }
        return "redirect:/itemsList";
    }
    @GetMapping(value = "/details/{idshka}")
    public String details(Model model,HttpSession session,@PathVariable(name="idshka") Long id){
        Items item=itemService.getItem(id);
        model.addAttribute("item",item);
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        if(getUserData() != null) {
            boolean isModer = false;
            for (Roles role : getUserData().getRoles()
            ) {
                if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_MODERATOR")) {
                    isModer = true;
                    model.addAttribute("isModer", isModer);
                    break;
                }
            }
        }
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        List<Pictures> pictures=itemService.getAllPicturesByItemId(id);
        List<Comments> comments=itemService.getAllCommentsByItem(id);
        model.addAttribute("comments",comments);
        model.addAttribute("categories",categories);
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("pictures",pictures);
        return "details";
    }
    @PostMapping(value = "/saveBrand")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String saveBrand(@RequestParam(name = "name",defaultValue = "No task") String name,
                            @RequestParam(name = "countryId",defaultValue = "0l") Long countryId,
                             @RequestParam(name = "id",defaultValue = "0l") Long id){



        Countries country=itemService.getCountry(countryId);
        Brands brand= itemService.getBrand(id);
        if(country!=null){
            brand.setName(name);
            brand.setCountry(country);
            itemService.saveBrand(brand);
        }



        return "redirect:/brandsList";
    }
    @PostMapping(value = "/deleteItem")
    @PreAuthorize ("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteTask(@RequestParam(name = "id",defaultValue = "0") Long id){
        itemService.deleteItem(itemService.getItem(id));
        return "redirect:/";
    }
    @PostMapping(value = "/deleteBrand")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String deleteBrand(@RequestParam(name = "id",defaultValue = "0") Long id){
        itemService.deleteBrand(itemService.getBrand(id));
        return "redirect:/brandsList";
    }
    @PostMapping(value = "/deleteCountry")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String deleteCountry(@RequestParam(name = "id",defaultValue = "0") Long id){
        itemService.deleteCountry(itemService.getCountry(id));
        return "redirect:/countryList";
    }
    @PostMapping(value = "/deleteCategory")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String deleteCategory(@RequestParam(name = "id",defaultValue = "0") Long id){
        itemService.deleteCategory(itemService.getCategory(id));
        return "redirect:/categoryList";
    }

    @PostMapping(value = "/saveCountry")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String saveCountry(@RequestParam(name = "name",defaultValue = "No task") String name,
                              @RequestParam(name = "code",defaultValue = "No task") String code,
                               @RequestParam(name = "id") Long id){




      Countries country=itemService.getCountry(id);
      country.setName(name);
      country.setCode(code);
      itemService.saveCountry(country);
        return "redirect:/countryList";
    }
    @PostMapping(value = "/saveRole")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String saveRole(@RequestParam(name = "name",defaultValue = "No task") String name,
                               @RequestParam(name = "description",defaultValue = "No task") String description,
                               @RequestParam(name = "id") Long id){




        Roles role=userService.getRole(id);
        role.setName(name);
        role.setDescription(description);
        userService.saveRole(role);
        return "redirect:/rolesList";
    }
    @GetMapping(value = "/rolesList")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String rolesList(Model model){
        List<Roles> roles=userService.gelAllRoles();
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("roles",roles);
        return "rolesList";
    }

    @PostMapping(value = "/deleteRole")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String deleteRole(@RequestParam(name = "id",defaultValue = "0") Long id){
        userService.deleteRole(userService.getRole(id));
        return "redirect:/rolesList";
    }

    @PostMapping(value = "/deleteUser")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam(name = "id",defaultValue = "0") Long id){
        userService.deleteUser(userService.getUser(id));
        return "redirect:/rolesList";
    }
    @PostMapping(value = "/addRole")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String addRole(@RequestParam(name = "name",defaultValue = "No task") String name,
                              @RequestParam(name = "description",defaultValue = "No task") String description){



        userService.addRole(new Roles(null,name,description));

        return "redirect:/rolesList";
    }
    @PostMapping(value = "/addUser")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String addRole(@RequestParam(name = "full_name",defaultValue = "No task") String fullname,
                           @RequestParam(name = "email",defaultValue = "No task") String email,
                           @RequestParam(name = "password",defaultValue = "0") String password,
                           @RequestParam(name = "repassword",defaultValue = "0") String repassword){


        String error="error";
        if(password.equals(repassword)){
            Users users = new Users();
            users.setEmail(email);
            users.setPassword(password);
            users.setFullName(fullname);
            if(userService.addUser(users)!=null){
                return "redirect:/usersList";
            }

        }

        return "redirect:/usersList?"+error;
    }
    @PostMapping(value = "/saveCategory")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public  String saveCategory(@RequestParam(name = "name",defaultValue = "No task") String name,
                               @RequestParam(name = "logoURL",defaultValue = "https://images.philips.com/is/image/PhilipsConsumer/50PUS6654_60-IMS-ru_RU?$jpglarge$&wid=1250") String logoURL,
                               @RequestParam(name = "id") Long id){




        Categories category=itemService.getCategory(id);
        category.setName(name);
        category.setLogoURL(logoURL);
        itemService.saveCategory(category);
        return "redirect:/categoryList";
    }
    @GetMapping(value = "/countryList")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String countryList(Model model){
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("countries",countries);
        return "countryList";
    }

    @GetMapping(value = "/itemsList")
    @PreAuthorize ("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String itemsList(Model model){
        List<Items> items=itemService.getAllItems();
        model.addAttribute("items",items);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("currentUser",getUserData());
        return "itemsList";
    }
    @GetMapping(value = "/categoryList")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String categoryList(Model model){
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        model.addAttribute("currentUser",getUserData());
        return "categoryList";
    }
    @GetMapping(value = "/brandsList")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String brandsList(Model model){
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("countries",countries);
        model.addAttribute("currentUser",getUserData());
        return "brandsList";
    }
    @GetMapping(value = "/detailsItems/{idshka}")
    @PreAuthorize ("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String detailsItems(Model model,@PathVariable(name="idshka") Long id){
        Items item=itemService.getItem(id);
        model.addAttribute("item",item);
        List<Categories> cat_item=item.getCategories();
        model.addAttribute("currentUser",getUserData());
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        List<Categories> categories1=itemService.getAllCategories();
        List<Pictures> pictures=itemService.getAllPicturesByItemId(id);
        if(categories!=null && cat_item!=null ){
            for(int i=0;i<categories.size();i++){
                if(categories.get(i)!=null && cat_item!=null ) {
                    for (int j = 0; j < cat_item.size(); j++) {
                        if(cat_item.get(j)!=null &&categories.get(i)!=null){
                            if (categories.get(i).getId().equals(cat_item.get(j).getId())) {
                                categories1.remove(categories.get(i));
                            }
                        }

                    }
                }
            }
        }

        model.addAttribute("cat_item",cat_item);
        model.addAttribute("categories",categories1);
        model.addAttribute("pictures",pictures);
        return "detailsItems";
    }
    @GetMapping(value = "/detailsCountry/{idshka}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String detailsCountry(Model model,@PathVariable(name="idshka") Long id){
        Countries country=itemService.getCountry(id);
        model.addAttribute("country",country);
        model.addAttribute("currentUser",getUserData());
        return "detailsCountry";
    }
    @GetMapping(value = "/detailsRole/{idshka}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String detailsRole(Model model,@PathVariable(name="idshka") Long id){
        Roles role=userService.getRole(id);
        System.out.println(role);
        model.addAttribute("role",role);
        model.addAttribute("currentUser",getUserData());
        return "detailsRole";
    }
    @GetMapping(value = "/detailsCategory/{idshka}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String detailsCategory(Model model,@PathVariable(name="idshka") Long id){
        Categories category=itemService.getCategory(id);
        model.addAttribute("category",category);
        model.addAttribute("currentUser",getUserData());
        return "detailsCategory";
    }
    @GetMapping(value = "/detailsBrands/{idshka}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String detailsBrands(Model model,@PathVariable(name="idshka") Long id){
        Brands brand=itemService.getBrand(id);
        model.addAttribute("brand",brand);
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("countries",countries);
        return "detailsBrands";
    }



    @GetMapping(value = "/searchByName")
    public  String search(Model model,HttpSession session,@RequestParam(name = "name",defaultValue = "0") String name){
            List<Items> items=itemService.getByName(name);
            model.addAttribute("itemsByn",items);
            model.addAttribute("name_search",name);
            List<Brands> brands=itemService.getAllBrands();
            model.addAttribute("brands",brands);
        model.addAttribute("currentUser",getUserData());
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
            return "result";
    }
    @GetMapping(value = "/searchByBrand/{id}")
    public  String searchByBrand(Model model,HttpSession session,@PathVariable(name = "id") Long id){
        model.addAttribute("currentUser",getUserData());
        List<Items> items=itemService.getItemsByBrand(id);
        model.addAttribute("itemsByn",items);
        model.addAttribute("brandsId",id);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        return "result";
    }
    @GetMapping(value = "/searchByCategory/{id}")
    public  String searchByCategory(Model model ,HttpSession session,@PathVariable(name = "id") Long id){
        List<Items> items=itemService.getAllByCategories(id);
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("itemsByn",items);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        System.out.println(items);
        return "result";
    }

    @GetMapping(value = "/searchByAll")
    public  String searchbyAll(Model model,HttpSession session,@RequestParam(name = "name",defaultValue = "") String name,
                                     @RequestParam(name="price1",defaultValue = "0") double price1,
                                     @RequestParam(name = "price2",defaultValue = "0") double price2,@RequestParam(name = "options1",defaultValue = "0") String option1,
                               @RequestParam(name="brandId",defaultValue = "0") Long brandsId){

        model.addAttribute("currentUser",getUserData());
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        if(name == null){
            name = "";
        }
        List<Items> filtered = itemService.getAllItems() ;
        if(name.equals("") && price1==0 && price2==0){
            if(option1.equals("asc")){
                filtered = itemService.findAllByOrderByPriceAsc();
            }
            else if(option1.equals("desc")){
                filtered = itemService.findAllByOrderByPriceDesc();
            }
        }
        else if(!name.equals("") && price1==0 && price2==0){
            if(option1.equals("asc")){
                filtered = itemService.findAllByNameLikeOrderByPriceAsc(name);
            }
            else if(option1.equals("desc")){
                filtered = itemService.findAllByNameLikeOrderByPriceDesc(name);
            }
        }
        else if(!name.equals("") && price1!=0 && price2==0){
            if(option1.equals("asc")){
                filtered = itemService.findAllByNameLikeAndPriceGreaterThanOrderByPriceAsc(name , price1);
            }
            else if(option1.equals("desc")){
                filtered = itemService.findAllByNameLikeAndPriceGreaterThanOrderByPriceDesc(name , price1);
            }
        }
        else if(!name.equals("") && price1==0 && price2!=0){
            if(option1.equals("asc")){
                filtered = itemService.findAllByNameLikeAndPriceLessThanOrderByPriceAsc(name , price2);
            }
            else if(option1.equals("desc")){
                filtered = itemService.findAllByNameLikeAndPriceLessThanOrderByPriceDesc(name , price2);
            }
        }
        else if(name.equals("") && price1!=0 && price2==0){

            if(option1.equals("asc")){
                filtered = itemService.findAllByPriceGreaterThanOrderByPriceAsc(price1);
            }
            else if(option1.equals("desc")){
                filtered = itemService.findAllByPriceGreaterThanOrderByPriceDesc(price1);
            }

        }
        else if(name.equals("") && price1==0 && price2!=0){
            if(option1.equals("asc")){
                filtered = itemService.findAllByPriceLessThanOrderByPriceAsc(price2);
            }
            else if(option1.equals("desc")){
                filtered = itemService.findAllByPriceLessThanOrderByPriceDesc(price2);
            }
        }
        else if (name.equals("") && price1!=0 && price2!=0){

            if(option1.equals("asc")){
                filtered = itemService. findAllByPriceBetweenOrderByPriceAsc(price1 , price2);
            }
            else if(option1.equals("desc")){
                filtered = itemService. findAllByPriceBetweenOrderByPriceDesc(price1 ,price2);
            }
        }
        else {
            if (option1.equals("asc")) {
                filtered = itemService.findAllByNameLikeAndPriceBetweenOrderByPriceAsc(name , price1 , price2);
            } else if (option1.equals("desc")) {
                filtered = itemService.findAllByNameLikeAndPriceBetweenOrderByPriceDesc(name , price1 , price2);
            }
        }
        filtered.removeIf(items -> !items.getBrands().getId().equals(brandsId));
        model.addAttribute("itemsByn" , filtered);
        model.addAttribute("brandId" , brandsId);
        model.addAttribute("CURRENT_USER" , getUserData());
        System.out.println(option1+" ");
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("name_search",name);

        return "result";

    }
    @PostMapping(value = "/assignCategory")
    @PreAuthorize ("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String assignCategory(@RequestParam(name="item_id") Long item_id,
                                 @RequestParam(name = "cat_id") Long categoryId){
        Categories cat=itemService.getCategory(categoryId);
        if(cat!=null){
            Items items=itemService.getItem(item_id);
            if(items!=null){
                List<Categories> categories=items.getCategories();
                if(categories==null){
                    categories=new ArrayList<>();
                }
                categories.add(cat);
                itemService.saveItem(items);
                return "redirect:/detailsItems/"+item_id;
            }
        }
        return "redirect:/";

    }
    @PostMapping(value = "/minusCategory")
    @PreAuthorize ("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String minusCategory(@RequestParam(name="item_id") Long item_id,
                                 @RequestParam(name = "cat_id") Long categoryId){
        Categories cat=itemService.getCategory(categoryId);
        if(cat!=null){
            Items items=itemService.getItem(item_id);
            if(items!=null){
                List<Categories> categories=items.getCategories();
                if(categories==null){
                    categories=new ArrayList<>();
                }
                categories.remove(cat);
                itemService.saveItem(items);
                return "redirect:/detailsItems/"+item_id;
            }
        }
        return "redirect:/";

    }

    @GetMapping(value="/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser",getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model,HttpSession session){
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");

        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            model.addAttribute("size",baskets.size());
        }
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize ("isAuthenticated()")
    public String profile(Model model ,HttpSession session){
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("countries",countries);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("currentUser",getUserData());
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");

        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        return "profile";
    }
    private Users getUserData(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser=(User)authentication.getPrincipal();
            Users myUser=userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

    @GetMapping(value = "/panel")
    @PreAuthorize ("hasAnyRole('ROLE_MODERATOR','ROLE_ADMIN')")
    public String panel(Model model){
        model.addAttribute("currentUser",getUserData());
        return "redirect:/itemsList";
    }

    @PostMapping(value = "/updateProfileByUser")
    @PreAuthorize ("isAuthenticated()")
    public String updateProfileByUser(Model model,@RequestParam(name = "user_fullName",defaultValue = "No task") String name){
        model.addAttribute("currentUser",getUserData());
        Users user=userService.getUser(getUserData().getId());
        user.setFullName(name);
        userService.saveUser(user);
        return "redirect:/profile";
    }
    @PostMapping(value = "/updatePasswordByUser")
    @PreAuthorize ("isAuthenticated()")
    public String updatePasswordByUser(Model model, @RequestParam(name = "user_passwordOld") String passwordOld,
                                       @RequestParam(name = "user_passwordNew")String passwordNew,
                                       @RequestParam(name="user_passwordNew2") String passwordNew2){
        model.addAttribute("currentUser",getUserData());
        Users user=userService.getUser(getUserData().getId());
        System.out.println(user);
        String error="";
        if(!passwordOld.equals("")&& !passwordNew.equals("") && !passwordNew2.equals("")){
            if(passwordEncoder.matches(passwordOld,user.getPassword())) {
                if (passwordNew.equals(passwordNew2)) {
                    user.setPassword(passwordEncoder.encode(passwordNew));
                    userService.saveUser(user);
                    System.out.println(user);
                    error="Password Updated Successfully";

                }
                else {
                    error="Passwords are not equal";
                }
            }
            else {
                error="Old Password is not correct!";
            }
        }
        else {
            error="Password can't be empty!";
        }
        model.addAttribute("currentUser" , getUserData());
        model.addAttribute("error" , error);
        return "profile";
    }


    @PostMapping(value = "/updateProfileByAdmin")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String updateProfileByAdmin(Model model,@RequestParam(name = "user_fullName",defaultValue = "No task") String name,
                                                    @RequestParam(name="id",defaultValue = "0")Long id ){

        Users user=userService.getUser(id);
        user.setFullName(name);
        userService.saveUser(user);
        return "redirect:/usersList";
    }
    @PostMapping(value = "/updatePasswordByAdmin")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String updatePasswordByAdmin(Model model, @RequestParam(name = "user_passwordOld",defaultValue = "No task") String passwordOld,
                                       @RequestParam(name = "user_passwordNew",defaultValue = "0")String passwordNew,
                                       @RequestParam(name="user_passwordNew2",defaultValue = "0") String passwordNew2,
                                        @RequestParam(name="id") Long id){

        Users user=userService.getUser(id);
        String error="";
        if(passwordEncoder.matches(passwordOld,user.getPassword())){
            if(passwordNew.equals(passwordNew2)){
                user.setPassword(passwordEncoder.encode(passwordNew));
                userService.saveUser(user);
                return "redirect:/usersList";
            }
            error="RetypeCorrect";
            return "redirect:/usersList?"+error;
        }
        error="OldPassword";
        return "redirect:/usersList?"+error;
    }
    @GetMapping(value = "/usersList")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String usersList(Model model){
        List<Roles> roles=userService.gelAllRoles();
        List<Users> users=userService.getAllUsers();
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("users",users);
        model.addAttribute("roles",roles);
        return "usersList";
    }
    @PostMapping(value = "/assignRole")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String assignRole(@RequestParam(name="user_id") Long user_id,
                                 @RequestParam(name = "role_id") Long role_id){
        Roles rol=userService.getRole(role_id);
        if(rol!=null){
            Users user=userService.getUser(user_id);
            if(user!=null){
                List<Roles> roles=user.getRoles();
                if(roles==null){
                    roles=new ArrayList<>();
                }
                roles.add(rol);
                userService.saveUser(user);
                return "redirect:/detailsUser/"+user_id;
            }
        }
        return "redirect:/";

    }
    @PostMapping(value = "/minusRole")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String minusRole(@RequestParam(name="user_id") Long user_id,
                                @RequestParam(name = "role_id") Long role_id){
        Roles rol=userService.getRole(role_id);
        if(rol!=null){
            Users user=userService.getUser(user_id);
            if(user!=null){
                List<Roles> roles=user.getRoles();
                if(roles==null){
                    roles=new ArrayList<>();
                }
                roles.remove(rol);
                userService.saveUser(user);
                return "redirect:/detailsUser/"+user_id;
            }
        }
        return "redirect:/";

    }
    @GetMapping(value = "/detailsUser/{idshka}")
    @PreAuthorize ("hasRole('ROLE_ADMIN')")
    public String detailsUser(Model model,@PathVariable(name="idshka") Long id){
        Users user=userService.getUser(id);
        model.addAttribute("user",user);
        List<Roles> role_user=user.getRoles();
        model.addAttribute("currentUser",getUserData());
        List<Roles> roles=userService.gelAllRoles();
        List<Roles> rolesList=userService.gelAllRoles();
        if(roles!=null && role_user!=null ){
            for(int i=0;i<roles.size();i++){
                if(roles.get(i)!=null && role_user!=null ) {
                    for (int j = 0; j < role_user.size(); j++) {
                        if(role_user.get(j)!=null &&roles.get(i)!=null){
                            if (roles.get(i).getId().equals(role_user.get(j).getId())) {
                                rolesList.remove(roles.get(i));
                            }
                        }

                    }
                }
            }
        }

        model.addAttribute("role_user",role_user);
        model.addAttribute("roles",rolesList);
        return "detailsUser";
    }


    @GetMapping(value = "/reg")
    @PreAuthorize("isAnonymous()")
    public String redirectReg(Model model,HttpSession session){
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }
        return "registration";
    }
    @PostMapping(value = "/registration")
    @PreAuthorize("isAnonymous()")
    public String registration(@RequestParam(name = "user_email") String user_email,
                               @RequestParam(name = "user_password") String user_password,
                               @RequestParam(name = "user_password2") String user_password2,
                               @RequestParam(name = "full_name") String full_name){
        if(user_password.equals(user_password2)){
            Users users = new Users();
            users.setEmail(user_email);
            users.setPassword(user_password);
            users.setFullName(full_name);
            if(userService.addUser(users)!=null){
                return "redirect:/login";
            }

        }
        return "redirect:/reg";

    }


    @PostMapping(value = "/uploadAvatar")
    @PreAuthorize("isAuthenticated()")
    public  String uploadAvatar(@RequestParam(name = "user_ava") MultipartFile file){
        if(file.getContentType().equals ("image/jpeg")||file.getContentType().equals("image/png")){
            try{
                Users currentUser=getUserData();
                String picName= DigestUtils.sha1Hex("avatar_"+currentUser.getId()+"_!Picture");
                byte[] bytes=file.getBytes();
                Path path= Paths.get(uploadPath+picName+".jpg");
                Files.write(path,bytes);
                currentUser.setUserAvatar(picName);
                userService.saveUser(currentUser);
                return "redirect:/profile?success";
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }
    @GetMapping(value = "/viewPhoto/{url}",produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException{
        String pictureURL=viewPath+defaultPicture;
        if(url!=null){
            pictureURL=viewPath+url+".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource=new ClassPathResource(pictureURL);
            in=resource.getInputStream();
        }
        catch (Exception e){
            ClassPathResource resource=new ClassPathResource(viewPath+defaultPicture);
            in=resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/uploadItemsPictures")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public  String uploadItemsPictures(@RequestParam(name = "item_picture") MultipartFile file,@RequestParam(name = "item_id") Long id){
        if(file.getContentType().equals ("image/jpeg")||file.getContentType().equals("image/png")){
            try{
                Items item=itemService.getItem(id);
                Pictures picture=new Pictures();
                long date=System.currentTimeMillis();
                Date addeddate=new Date(date);
                picture.setAddedDate(addeddate);
                picture.setItems(item);
                picture=itemService.addPicture(picture);
                String picName= DigestUtils.sha1Hex("item_"+item.getId()+picture.getId()+"_!Picture");

                byte[] bytes=file.getBytes();
                Path path= Paths.get(uploadPathPictures+picName+".jpg");
                Files.write(path,bytes);
                picture.setUrl(picName);
                itemService.savePicture(picture);

                return "redirect:/detailsItems/"+id;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "redirect:/itemsList";
    }
    @GetMapping(value = "/viewItemsPictures/{url}",produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewItemsPictures(@PathVariable(name = "url") String url) throws IOException{
        String pictureURL=viewPathPictures+defaultPictureItems;
        if(url!=null){
            pictureURL=viewPathPictures+url+".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource=new ClassPathResource(pictureURL);
            in=resource.getInputStream();
        }
        catch (Exception e){
            ClassPathResource resource=new ClassPathResource(viewPathPictures+defaultPictureItems);
            in=resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/deletePictures")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deletePictures(@RequestParam(name = "picture_id") Long id,@RequestParam(name = "item_id") Long item_id){
        Pictures picture=itemService.getPicture(id);
        if(picture!=null){
            itemService.deletePicture(picture);
            return "redirect:/detailsItems/"+item_id;
        }
        return "redirect:/itemsList";
    }

    @GetMapping(value = "/basket")
    public String basket(Model model, HttpSession session){
        List<Countries> countries=itemService.getAllCountry();
        model.addAttribute("countries",countries);
        List<Brands> brands=itemService.getAllBrands();
        model.addAttribute("brands",brands);
        List<Categories> categories=itemService.getAllCategories();
        model.addAttribute("categories",categories);
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        model.addAttribute("currentUser", getUserData());
        int total = 0;
        if(baskets == null){
            baskets = new ArrayList<>();
        }
        for(Basket b:baskets){
            total += (b.getQuantity() * b.getItems().getPrice());

        }

        model.addAttribute("basket", baskets);
        model.addAttribute("total", total);

        if(baskets==null){
            model.addAttribute("size",0);
        }
        else{
            int size =0;
            for (Basket b: baskets) {
                size+=b.getQuantity();
            }
            model.addAttribute("size",size);
        }

        return "basket";
    }

    @GetMapping(value = "/orderList")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String orderList(Model model){

        model.addAttribute("currentUser", getUserData());
        int total = 0;
        List<Orders> orders=itemService.getAllOrders();
        model.addAttribute("orders",orders);
        return "orderList";
    }

    @PostMapping(value = "/addBasket")
    public String addBasket(HttpSession session,
                            @RequestParam(name = "id") Long id){
        List<Basket> items = (List<Basket>) session.getAttribute("basketItems");
        if(items == null){
            items = new ArrayList<>();

        }

        Basket basket = new Basket();
        Items items1 = itemService.getItem(id);
        basket.setItems(items1);
        System.out.println(basket);

        if(items.size() > 0){
            for (Basket b: items){
                if(b.compareTo(basket) > 0){
                    int existQuantity = b.getQuantity();
                    b.setQuantity(existQuantity+1);
                    session.setAttribute("basketItems", items);
                    System.out.println(items);
                    return "redirect:/basket";
                }
            }
        }

        basket.setQuantity(basket.getQuantity() + 1);
        items.add(basket);
        session.setAttribute("basketItems", items);

        System.out.println(items);

        return "redirect:/basket";
    }

    @PostMapping(value = "/addQuantity")
    public String addQuantity(HttpSession session,
                            @RequestParam(name = "id") Long id){

        List<Basket> items = (List<Basket>) session.getAttribute("basketItems");
        Items item = itemService.getItem(id);
        Basket basket = new Basket();
        basket.setItems(item);

        for(Basket b: items){
            if(b.compareTo(basket)>0){
                b.setQuantity(b.getQuantity() + 1);
                session.setAttribute("basketItems", items);
                return "redirect:/basket";
            }
        }
        return "redirect:/basket";
    }

    @PostMapping(value = "/minusQuantity")
    public String removeQuantity(HttpSession session,
                                 @RequestParam(name = "id") Long id){
        List<Basket> items = (List<Basket>) session.getAttribute("basketItems");

        Items item = itemService.getItem(id);
        Basket basket = new Basket();
        basket.setItems(item);

        for(Basket b: items){
            if(b.compareTo(basket)>0){
                if (b.getQuantity() <= 1){
                    items.remove(b);
                    session.setAttribute("basketItems", items);
                    return "redirect:/basket";

                }

                b.setQuantity(b.getQuantity() - 1);
                session.setAttribute("basketItems", items);
                return "redirect:/basket";

            }
        }


        return "redirect:/basket";
    }



    @PostMapping(value = "/clearBasket")
    public String clearBasket(HttpSession session){
        session.removeAttribute("basketItems");

        return "redirect:/basket";
    }


    @PostMapping(value = "/checkIn")
    public String checkIn(HttpSession session,
                          @RequestParam(name = "full_name") String full_name){
        List<Basket> baskets = (List<Basket>) session.getAttribute("basketItems");
        long date=System.currentTimeMillis();
        Date addeddate=new Date(date);

        for(Basket b:baskets){
            Orders order = new Orders();
            order.setAddedDate(addeddate);
            order.setFullName(full_name);
            order.setItems(b.getItems());
            itemService.addOrder(order);
        }
        session.removeAttribute("basketItems");
        return "redirect:/basket";
    }

    @PostMapping(value = "/addComment")

    public String addComment(HttpSession session,
                          @RequestParam(name = "comment") String comment,@RequestParam(name = "item_id") Long item_id){

        long date=System.currentTimeMillis();
        Date addeddate=new Date(date);
        Comments comments=new Comments();
        comments.setAddedDate(addeddate);
        comments.setComment(comment);
        comments.setAuthors(getUserData());
        Items items=itemService.getItem(item_id);

        comments.setItems(items);
        itemService.addComment(comments);
        return "redirect:/details/"+item_id;
    }
    @PostMapping(value = "/updateComment")
    public String updateComment(HttpSession session,
                             @RequestParam(name = "comment") String comment,@RequestParam(name = "item_id") Long item_id,
                                @RequestParam(name = "comment_id") Long comment_id){


        Comments comments=itemService.getComment(comment_id);
        comments.setComment(comment);
        itemService.saveComment(comments);
        return "redirect:/details/"+item_id;
    }
    @PostMapping(value = "/deleteComment")
    public String deleteComment(HttpSession session,
                                @RequestParam(name = "item_id") Long item_id,
                                @RequestParam(name = "comment_id") Long comment_id){


        Comments comments=itemService.getComment(comment_id);

        itemService.deleteComment(comments);
        return "redirect:/details/"+item_id;
    }
    @GetMapping(value = "/commentsList")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String commentsList(Model model){

        model.addAttribute("currentUser", getUserData());
        int total = 0;
        List<Comments> comments=itemService.gelAllComments();
        model.addAttribute("comments",comments);
        return "commentsList";
    }
    @PostMapping(value = "/deleteCommentByAdmin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteCommentByAdmin(@RequestParam(name = "comment_id") Long comment_id){


        Comments comments=itemService.getComment(comment_id);

        itemService.deleteComment(comments);
        return "redirect:/commentsList";
    }
}
