package com.cozyhome.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozyhome.onlineshop.model.BasketRecord;

public interface ShoppingCartRepository extends JpaRepository<BasketRecord, Integer>{

}
