package com.api.epharmacy.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.epharmacy.service.CategoryService;
import com.api.epharmacy.ui.model.request.CategoryRequestModel;
import com.api.epharmacy.ui.model.request.InventoryCategoryRequestModel;
import com.api.epharmacy.ui.model.response.CategoryResponseModel;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@PostMapping
	public CategoryResponseModel saveCategory(@RequestBody CategoryRequestModel categoryDetail) {
		CategoryResponseModel returnValue = categoryService.saveCategory(categoryDetail);
		return returnValue;

	}

	@GetMapping
	public List<CategoryResponseModel> getAllCategories(
			@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "1000") int limit) {

		List<CategoryResponseModel> returnValue = categoryService.getAllCategories(searchKey, page, limit);

		return returnValue;

	}

	@GetMapping(path = "/{categoryId}")
	public CategoryResponseModel getCategory(@PathVariable Integer categoryId, HttpServletRequest headerRequest) {

		CategoryResponseModel returnValue = categoryService.getCategory(categoryId);
		return returnValue;
	}

	@PutMapping(path = "/{categoryId}")
	public CategoryResponseModel updateCategory(@PathVariable Integer categoryId,
			@RequestBody CategoryRequestModel categoryDetail) {
		CategoryResponseModel returnValue = categoryService.updateCategory(categoryId, categoryDetail);
		return returnValue;
	}

	@DeleteMapping(path = "/{categoryId}")
	public String deleteCategory(@PathVariable Integer categoryId) {

		String returnValue = categoryService.deleteCategory(categoryId);
		return returnValue;
	}
	
	@PostMapping(path = "/import")
	public String importInventoryCategory(@ModelAttribute InventoryCategoryRequestModel importInventoryCategoryDetails)
	        throws IOException {
		
		String returnValue = categoryService.importInventoryCategory(importInventoryCategoryDetails);
		
		return returnValue;
	}

}
