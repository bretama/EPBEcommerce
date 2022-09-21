package com.api.epharmacy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.epharmacy.service.CustomerInventorySellPriceService;
import com.api.epharmacy.ui.model.request.CustomerInventorySellPriceRequestModel;
import com.api.epharmacy.ui.model.response.CustomerInventorySellPriceResponseModel;


@RestController
@RequestMapping("/customer-inventory-sell-price")
public class CustomerInventorySellPriceController {
	
	@Autowired
	CustomerInventorySellPriceService customerInventorySellPriceService;
	
	@PostMapping
	public CustomerInventorySellPriceResponseModel saveCustomerInventorySelllPrice(@RequestBody CustomerInventorySellPriceRequestModel customerInventorySellPriceDetail) {
		CustomerInventorySellPriceResponseModel returnValue = customerInventorySellPriceService.saveCustomerInventorySelllPrice(customerInventorySellPriceDetail);
		return returnValue;
	}
	
	@GetMapping
	public List<CustomerInventorySellPriceResponseModel> getAllCustomerInventorySellPrices(
			@RequestParam(value = "inventoryId", defaultValue = "") long inventoryId,
			@RequestParam(value = "companyId", defaultValue = "0") long companyId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "1000") int limit){
		List<CustomerInventorySellPriceResponseModel> returnValue = customerInventorySellPriceService.getAllCustomerInventorySellPrices(page,limit,inventoryId, companyId);
		return returnValue;
	}
	
	@GetMapping(path = "/{customerInventorySellPriceId}")
	public CustomerInventorySellPriceResponseModel getCustomerInventorySellPrice(@PathVariable long customerInventorySellPriceId) {
		CustomerInventorySellPriceResponseModel returnValue = customerInventorySellPriceService.getCustomerInventorySellPrice(customerInventorySellPriceId);
		return returnValue;
	}
	
	@PutMapping(path = "/{customerInventorySellPriceId}")
	public CustomerInventorySellPriceResponseModel updateCustomerInventorySellPrice(@PathVariable long customerInventorySellPriceId, @RequestBody CustomerInventorySellPriceRequestModel customerInventorySellPriceDetail) {
		CustomerInventorySellPriceResponseModel returnValue = customerInventorySellPriceService.updateCustomerInventorySellPrice(customerInventorySellPriceId, customerInventorySellPriceDetail);
		return returnValue;
	}
	
	@DeleteMapping(path = "/{customerInventorySellPriceId}")
	public String deleteCustomerInventorySellPrice(@PathVariable long customerInventorySellPriceId) {
		String returnValue = customerInventorySellPriceService.deleteCustomerInventorySellPrice(customerInventorySellPriceId);
		return returnValue;
	}

}
