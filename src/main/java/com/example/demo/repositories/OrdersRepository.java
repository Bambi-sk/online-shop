package com.example.demo.repositories;

import com.example.demo.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
