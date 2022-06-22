package com.example.demo.repositories;

import com.example.demo.entities.Brands;
import com.example.demo.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BrandsRepository extends JpaRepository<Brands,Long> {

}
