package com.cozyhome.onlineshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.model.Inventory;
import com.cozyhome.onlineshop.model.ProductColor;
import com.cozyhome.onlineshop.model.BasketRecord;
import com.cozyhome.onlineshop.repository.InventoryRepository;
import com.cozyhome.onlineshop.repository.ItemLineRepository;
import com.cozyhome.onlineshop.repository.ProductColorRepository;
import com.cozyhome.onlineshop.repository.ShoppingCartRepository;
import com.cozyhome.onlineshop.repository.UserRepository;
import com.cozyhome.onlineshop.util.CellIndex;
import com.cozyhome.onlineshop.util.DataReader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostgresBuilder {
	private final DataReader reader;
	private final ProductColorRepository productColorRepo;
	private final InventoryRepository inventoryRepo;
	private final ItemLineRepository shoppingCartRepo;
	private final UserRepository userRepo;
	private final ShoppingCartRepository cartRepo;

	private Map<String, String> colors = new HashMap<>();
	private List<Integer> colorIndexes = new ArrayList<>();

	{
		colors.put("Сірий", "#545454");
		colors.put("Чорний", "#262626");
		colors.put("Коричневий", "#C57100");

		colorIndexes.add(CellIndex.PRODUCT_COLOR_1);
		colorIndexes.add(CellIndex.PRODUCT_COLOR_2);
		colorIndexes.add(CellIndex.PRODUCT_COLOR_3);
	}

	@Transactional
	public void buildProductColorTable() {
		log.info("1 STEP");
		int rowIndex = 2;
		String productSkuCode = "";
		while (!(productSkuCode = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_SKU)).isEmpty()) {
			for (int i = 0; i < colorIndexes.size(); i++) {
				String color = reader.readFromExcel(rowIndex, colorIndexes.get(i)).trim();
				if (!color.isEmpty()) {
					doBuildProductColor(productSkuCode, color);
				}
			}

			int nextIndex = rowIndex + 1;
			if (reader.readFromExcel(nextIndex, CellIndex.PRODUCT_SKU).isEmpty()) {
				rowIndex++;
			}

			rowIndex++;
		}
	}

	@Transactional
	public void buildInventory(ProductColor productColor) {
		Inventory inventory = new Inventory();
		inventory.setProductColor(productColor);
		inventory.setQuantity(new Random().nextInt(7));
		inventoryRepo.save(inventory);
		log.info("INVENTORY FOR PRODUCT_SKUCODE [" + productColor.getProductSkuCode() + "] WITH COLOR_HEX ["
				+ productColor.getColorHex() + "]  SAVED.");
	}

	private ProductColor doBuildProductColor(String productSkuCode, String color) {
		ProductColor productColor = new ProductColor();
		ProductColor savedProductColor = new ProductColor();
		if (!color.isEmpty()) {
			String colorHex = colors.get(color);
			if (colorHex != null) {
				productColor.setProductSkuCode(productSkuCode);
				productColor.setColorHex(colorHex);
				savedProductColor = productColorRepo.save(productColor);
				log.info("PRODUCT_COLOR [SKUCODE:" + productSkuCode + ",HEX:" + colorHex + "] SAVED.");
			}
		}
		return savedProductColor;
	}

	public void buildBasket(ProductColor productColor, String userId) {
		BasketRecord basketRecordToSave = BasketRecord.builder()
				.productColor(productColor)
				.quantity(new Random().nextInt(1,4))
				.userId(userId)
				.build();
		BasketRecord basketRecordSaved = shoppingCartRepo.save(basketRecordToSave);
		log.info("BASKET RECORD WITH SKU CODE [{}] AND COLOR HEX [{}] SAVED.", basketRecordSaved.getProductColor().getProductSkuCode(),
				basketRecordSaved.getProductColor().getColorHex());
	}
	
}
