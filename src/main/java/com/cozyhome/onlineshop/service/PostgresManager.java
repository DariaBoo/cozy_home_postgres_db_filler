package com.cozyhome.onlineshop.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.model.Inventory;
import com.cozyhome.onlineshop.model.ProductColor;
import com.cozyhome.onlineshop.repository.InventoryRepository;
import com.cozyhome.onlineshop.repository.ProductColorRepository;
import com.cozyhome.onlineshop.util.SqlExecuter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostgresManager {
	private final SqlExecuter executer;
	private final PostgresBuilder builder;
	private final ProductColorRepository productColorRepo;
	private final InventoryRepository inventoryRepo;
	private String createDbFile = "/create_db.sql";
	private String createUserFile = "/create_user.sql";
	private String createTablesFile = "/create_tables.sql";

	public void createDataBase() {
		log.info("0 STEP[CREATE DATABASE]");
//		executer.executeSqlFromFile(createUserFile);
//		executer.executeSqlFromFile(createDbFile);
		executer.executeSqlFromFile(createTablesFile);

		builder.buildProductColorTable();
		fillInventory();
	}

	private void fillInventory() {
		List<ProductColor> productColorList = productColorRepo.findAll();
		for (int i = 0; i < productColorList.size(); i++) {
			builder.buildInventory(productColorList.get(i));			
		}
	}
	
	public void getInventory() {
		List<Inventory> list = inventoryRepo.findAll();
		list.stream().forEach(System.out::println);
	}
}
