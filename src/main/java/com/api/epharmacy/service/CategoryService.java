package com.api.epharmacy.service;

import java.io.IOException;
import java.util.List;

import com.api.epharmacy.ui.model.request.CategoryRequestModel;
import com.api.epharmacy.ui.model.request.InventoryCategoryRequestModel;
import com.api.epharmacy.ui.model.response.CategoryResponseModel;

public interface CategoryService {

	CategoryResponseModel saveCategory(CategoryRequestModel categoryDetail);

	List<CategoryResponseModel> getAllCategories(String searchKey, int page, int limit);

	CategoryResponseModel getCategory(Integer categoryId);

	CategoryResponseModel updateCategory(Integer categoryId, CategoryRequestModel categoryDetail);

	String deleteCategory(Integer categoryId);

	String importInventoryCategory(InventoryCategoryRequestModel importInventoryCategoryDetails) throws IOException;
	
}
