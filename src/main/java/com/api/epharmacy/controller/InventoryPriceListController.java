package com.api.epharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.epharmacy.io.repositories.InventorySellPriceListRepository;
import com.api.epharmacy.service.InventoryPriceListService;
import com.api.epharmacy.ui.model.response.InventorySellPriceDetail;

@RestController
@RequestMapping("/pricelist")
public class InventoryPriceListController {
	
	@Autowired
	InventoryPriceListService inventoryPriceListService;
	
	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;
	@GetMapping(path="/{InventoryId}")
	public InventorySellPriceDetail getInventoryPriceList(@PathVariable long InventoryId,
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        @RequestParam(value = "limit", defaultValue = "25") int limit) {
		
		return inventoryPriceListService.getInventoryItemPriceList(InventoryId, page, limit);
	}
}
