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
import com.api.epharmacy.service.CustomerSoldInventoryService;
import com.api.epharmacy.ui.model.request.CustomerSoldInventoryRequestModel;
import com.api.epharmacy.ui.model.response.CustomerSoldInventoryResponseModel;

@RestController
@RequestMapping("/customer-sold-inventory")
public class CustomerSoldInventoryController {
	
	@Autowired
	CustomerSoldInventoryService customerSoldInventoryService;
	
	@PostMapping
	public CustomerSoldInventoryResponseModel saveCustomerSoldInventory(@RequestBody CustomerSoldInventoryRequestModel customerSoldInventoryDetail) {
		CustomerSoldInventoryResponseModel returnValue = customerSoldInventoryService.saveCustomerSoldInventory(customerSoldInventoryDetail);
		return returnValue;
	}
	
	@GetMapping
	public List<CustomerSoldInventoryResponseModel> getAllCustomerSoldInventories(
			@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "1000") int limit,
			@RequestParam(value = "companyId", defaultValue = "") long companyId,
			@RequestParam(value = "userType", defaultValue = "") String userType){
		List<CustomerSoldInventoryResponseModel> returnValue = customerSoldInventoryService.getAllCustomerSoldInventories(page,limit,searchKey, companyId, userType);
		return returnValue;
	}
	
	@GetMapping(path = "/{customerSoldInventoryId}")
	public CustomerSoldInventoryResponseModel getCustomerSoldInventory(@PathVariable long customerSoldInventoryId) {
		CustomerSoldInventoryResponseModel returnValue = customerSoldInventoryService.getCustomerSoldInventory(customerSoldInventoryId);
		return returnValue;
	}
	
	@PutMapping(path = "/{customerSoldInventoryId}")
	public CustomerSoldInventoryResponseModel updateCustomerSoldInventory(@PathVariable long customerSoldInventoryId, @RequestBody CustomerSoldInventoryRequestModel customerSoldInventoryDetail) {
		CustomerSoldInventoryResponseModel returnValue = customerSoldInventoryService.updateCustomerInventoryTransaction(customerSoldInventoryId, customerSoldInventoryDetail);
		return returnValue;
	}
	
	@DeleteMapping(path = "/{customerSoldInventoryId}")
	public String deleteCustomerSoldInventory(@PathVariable long customerSoldInventoryId) {
		String returnValue = customerSoldInventoryService.deleteCustomerSoldInventory(customerSoldInventoryId);
		return returnValue;
	}
}
