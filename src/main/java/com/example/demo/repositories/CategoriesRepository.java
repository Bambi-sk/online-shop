package com.example.demo.repositories;

import com.example.demo.entities.Categories;
import com.example.demo.entities.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Repository
@Transactional
public interface CategoriesRepository extends JpaRepository<Categories,Long> {
}



