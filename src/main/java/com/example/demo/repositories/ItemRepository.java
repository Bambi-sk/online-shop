package com.example.demo.repositories;


import com.example.demo.db.Item;
import com.example.demo.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Items,Long> {
        List<Items> findAllByInTopPageIsTrue();
        List<Items> findAllByNameLike(String name);
        List<Items> findAllByNameLikeOrderByPriceAsc(String name);

        List<Items> findAllByNameLikeOrderByPriceDesc(String name);

        List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2);

        List<Items> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2);

        List<Items> findAllByOrderByPriceDesc();

        List<Items> findAllByOrderByPriceAsc();

        List<Items> findAllByNameLikeAndPriceGreaterThanOrderByPriceAsc(String name, double from);

        List<Items> findAllByNameLikeAndPriceGreaterThanOrderByPriceDesc(String name, double from);

        List<Items> findAllByNameLikeAndPriceLessThanOrderByPriceAsc(String name, double from);
        List<Items> findAllByNameLikeAndPriceLessThanOrderByPriceDesc(String name, double from);

        List<Items> findAllByPriceGreaterThanOrderByPriceAsc(double from);
        List<Items> findAllByPriceGreaterThanOrderByPriceDesc(double from);
        List<Items> findAllByPriceLessThanOrderByPriceAsc(double to);
        List<Items> findAllByPriceLessThanOrderByPriceDesc(double to);

        List<Items> findAllByPriceBetweenOrderByPriceAsc(double price1, double price2);

        List<Items> findAllByPriceBetweenOrderByPriceDesc(double price1, double price2);



        List<Items> findAllByCategoriesIdOrderByPriceAsc(Long id);
        List<Items> findAllByBrandsId(Long id);
        List<Items> findAllByCategoriesId(Long id);
}
