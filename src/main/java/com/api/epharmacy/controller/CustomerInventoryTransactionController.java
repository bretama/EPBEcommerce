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

import com.api.epharmacy.service.CustomerInventoryTransactionService;
import com.api.epharmacy.ui.model.request.CustomerInventoryTransactionRequestModel;
import com.api.epharmacy.ui.model.response.CustomerInventoryTransactionResponseModel;

@RestController
@RequestMapping("/customer-inventory-transaction")
public class CustomerInventoryTransactionController {
	
	@Autowired
	CustomerInventoryTransactionService customerInventoryTransactionService;
	
	@PostMapping
	public CustomerInventoryTransactionResponseModel saveCustomerInventoryTransaction(@RequestBody CustomerInventoryTransactionRequestModel customerInventoryTransactionDetail) {
		CustomerInventoryTransactionResponseModel returnValue = customerInventoryTransactionService.saveCustomerInventoryTransaction(customerInventoryTransactionDetail);
		return returnValue;
	}
	
	@GetMapping
	public List<CustomerInventoryTransactionResponseModel> getAllCustomerInventoryTransactions(
			@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
			@RequestParam(value = "companyId", defaultValue = "") long companyId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "1000") int limit){
		List<CustomerInventoryTransactionResponseModel> returnValue = customerInventoryTransactionService.getAllCustomerInventoryTransactions(page,limit,searchKey, companyId);
		return returnValue;
	}
	
	@GetMapping(path = "/filtered-inventories")
	public List<CustomerInventoryTransactionResponseModel> getAllCustomerFilteredInventories(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "1000") int limit){
		List<CustomerInventoryTransactionResponseModel> returnValue = customerInventoryTransactionService.getAllCustomerFilteredInventories(page,limit,searchKey);
		return returnValue;
	}
	
	@GetMapping(path = "/{customerInventoryTransactionId}")
	public CustomerInventoryTransactionResponseModel getCustomerInventoryTransaction(@PathVariable long customerInventoryTransactionId) {
		CustomerInventoryTransactionResponseModel returnValue = customerInventoryTransactionService.getCustomerInventoryTransaction(customerInventoryTransactionId);
		return returnValue;
	}
	
	@PutMapping(path = "/{customerInventoryTransactionId}")
	public CustomerInventoryTransactionResponseModel updateCustomerInventoryTransaction(@PathVariable long customerInventoryTransactionId, @RequestBody CustomerInventoryTransactionRequestModel customerInventoryTransactionDetail) {
		CustomerInventoryTransactionResponseModel returnValue = customerInventoryTransactionService.updateCustomerInventoryTransaction(customerInventoryTransactionId, customerInventoryTransactionDetail);
		return returnValue;
	}
	
	@DeleteMapping(path = "/{customerInventoryTransactionId}")
	public String deleteCustomerInventoryTransaction(@PathVariable long customerInventoryTransactionId) {
		String returnValue = customerInventoryTransactionService.deleteCustomerInventoryTransaction(customerInventoryTransactionId);
		return returnValue;
	}

}
