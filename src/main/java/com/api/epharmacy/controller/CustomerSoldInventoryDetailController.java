package com.api.epharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.epharmacy.service.CustomerSoldInventoryDetailService;
import com.api.epharmacy.ui.model.request.CustomerSoldInventoryDetailRequestModel;
import com.api.epharmacy.ui.model.response.CustomerSoldInventoryDetailResponseModel;

@RestController
@RequestMapping("/customer-sold-inventory-detail")
public class CustomerSoldInventoryDetailController {
	
	@Autowired
	CustomerSoldInventoryDetailService customerSoldInventoryDetailService;
	
	@PutMapping(path = "/{customerSoldInventoryDetailId}")
	public CustomerSoldInventoryDetailResponseModel updateCustomerSoldInventoryDetail(@RequestBody CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetail, @PathVariable long customerSoldInventoryDetailId) {
		CustomerSoldInventoryDetailResponseModel returnValue = customerSoldInventoryDetailService.updateCustomerSoldInventoryDetail(customerSoldInventoryDetail,customerSoldInventoryDetailId);
		return returnValue;
		
	}
	
	@GetMapping(path = "/{customerSoldInventoryDetailId}")
	public CustomerSoldInventoryDetailResponseModel getCustomerSoldInventoryDetail(@PathVariable long customerSoldInventoryDetailId) {
		CustomerSoldInventoryDetailResponseModel returnValue = customerSoldInventoryDetailService.getCustomerSoldInventoryDetail(customerSoldInventoryDetailId);
		return returnValue;
		
	}
	
	@DeleteMapping(path="/{customerSoldInventoryDetailId}")
	public String deleteCustomerSoldInventoryDetail(@PathVariable long customerSoldInventoryDetailId) {
		String returnValue = customerSoldInventoryDetailService.deleteCustomerSoldInventoryDetail(customerSoldInventoryDetailId);
		return returnValue;
	
	}

}
