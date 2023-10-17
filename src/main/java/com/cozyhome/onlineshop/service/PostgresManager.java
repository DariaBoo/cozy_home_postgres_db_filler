package com.cozyhome.onlineshop.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.model.Inventory;
import com.cozyhome.onlineshop.model.ProductColor;
import com.cozyhome.onlineshop.model.User;
import com.cozyhome.onlineshop.repository.InventoryRepository;
import com.cozyhome.onlineshop.repository.ProductColorRepository;
import com.cozyhome.onlineshop.repository.UserRepository;
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
	private final UserRepository userRepo;
	
	private String createDbFile = "/create_db.sql";
	private String createUserFile = "/create_user.sql";
	private String createTablesFile = "/create_tables.sql";
	private String createTableShoppingCartLines = "/create_table_shopping_carts.sql";

	public void createDataBase() {
		log.info("0 STEP[CREATE DATABASE]");
//		executer.executeSqlFromFile(createUserFile);
//		executer.executeSqlFromFile(createDbFile);
//		executer.executeSqlFromFile(createTablesFile);

//		builder.buildProductColorTable();
//		fillInventory();
		executer.executeSqlFromFile(createTableShoppingCartLines);
		fillBasket();
	}

	private void fillInventory() {
		List<ProductColor> productColorList = productColorRepo.findAll();
		for (int i = 0; i < productColorList.size(); i++) {
			builder.buildInventory(productColorList.get(i));			
		}
	}
	
	public void fillBasket() {
		List<ProductColor> productColorList = productColorRepo.findAll();
		List<User> users = userRepo.findAll();
		for (ProductColor productColor : productColorList) {
			builder.buildBasket(productColor,users.get(new Random().nextInt(users.size())).getId());			
		}		
	}
	public void getInventory() {
		List<Inventory> list = inventoryRepo.findAll();
		list.stream().forEach(System.out::println);
	}
}
