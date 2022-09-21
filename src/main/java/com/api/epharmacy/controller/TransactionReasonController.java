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

import com.api.epharmacy.service.TransactionReasonService;
import com.api.epharmacy.ui.model.request.TransactionReasonRequestModel;
import com.api.epharmacy.ui.model.response.TransactionReasonResponseModel;

@RestController
@RequestMapping("/transaction-reason")
public class TransactionReasonController {
	
	@Autowired
	TransactionReasonService transactionReasonService;
	
	@PostMapping
	public TransactionReasonResponseModel saveTransctionReason(@RequestBody TransactionReasonRequestModel transactionReasonDetail) {
		TransactionReasonResponseModel returnValue = transactionReasonService.saveTransctionReason(transactionReasonDetail);
		return returnValue;
	}
	@GetMapping
	public List<TransactionReasonResponseModel> getAllTransactionReasons(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit, @RequestParam(value = "searchKey", defaultValue = "") String searchKey) {
		List<TransactionReasonResponseModel> returnValue = transactionReasonService.getAllTransactionReasons(page,limit,searchKey);
		return returnValue;
	}
	
	@GetMapping(path = "/{transactionReasonId}")
	public TransactionReasonResponseModel getTransactionReason(@PathVariable Integer transactionReasonId) {
		TransactionReasonResponseModel returnValue = transactionReasonService.getTransactionReason(transactionReasonId);
		return returnValue;
	}
	
	@PutMapping(path = "/{transactionReasonId}")
	public TransactionReasonResponseModel updateTransactionReason(@RequestBody TransactionReasonRequestModel transactionReasonDetail, @PathVariable Integer transactionReasonId) {
		TransactionReasonResponseModel returnValue = transactionReasonService.updateTransactionReason(transactionReasonDetail, transactionReasonId);
		return returnValue;
	}
	@DeleteMapping(path = "/{transactionReasonId}")
	public String deleteTransactionReason(@PathVariable Integer transactionReasonId) {
		String returnValue = transactionReasonService.deleteTransactionReason(transactionReasonId);
		return returnValue;
	}
}
