package com.cozyhome.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozyhome.onlineshop.model.BasketRecord;

public interface ItemLineRepository extends JpaRepository<BasketRecord, Integer>{

}
