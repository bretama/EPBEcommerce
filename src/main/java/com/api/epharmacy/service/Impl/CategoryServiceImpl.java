package com.api.epharmacy.service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.epharmacy.exception.AppException;
import com.api.epharmacy.io.entity.CategoryEntity;
import com.api.epharmacy.io.repositories.CategoryRepository;
import com.api.epharmacy.service.CategoryService;
import com.api.epharmacy.ui.model.request.CategoryRequestModel;
import com.api.epharmacy.ui.model.request.InventoryCategoryRequestModel;
import com.api.epharmacy.ui.model.response.CategoryResponseModel;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public CategoryResponseModel saveCategory(CategoryRequestModel categoryDetail) {
		
		CategoryResponseModel returnValue = new CategoryResponseModel();
		CategoryEntity categoryEntity = new CategoryEntity();
		
		BeanUtils.copyProperties(categoryDetail, categoryEntity);
		
		CategoryEntity checkCategory = categoryRepository.findByInventoryTypeAndIsDeleted(categoryDetail.getInventoryType(),
		    false);
		if (checkCategory != null)
			throw new AppException("Category already exists");
		//		categoryEntity.setName(categoryDetail.getName());
		CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);
		
		BeanUtils.copyProperties(savedCategoryEntity, returnValue);
		return returnValue;
	}

	@Override
	public List<CategoryResponseModel> getAllCategories(String searchKey, int page, int limit) {
		
		 List<CategoryResponseModel> returnValue = new ArrayList<>();
		    
		    if(page > 0) page = page - 1; 
		   
		    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("inventoryType").ascending());
		    Page<CategoryEntity> categoryPage = null;
		    
		    if("".equals(searchKey))
		    	categoryPage = categoryRepository.findByIsDeleted(false, pageableRequest);//.findAll(pageableRequest);
		    else
		    	categoryPage = categoryRepository.findByIsDeletedAndInventoryTypeContaining(false,searchKey, pageableRequest);//.findAll(pageableRequest);
		    
		    List<CategoryEntity> categories = categoryPage.getContent();
		    
		    
		    
		    int totalPages = categoryPage.getTotalPages();	    
		    for(CategoryEntity categoryEntity : categories) {
		    	
		    	CategoryResponseModel categoryResponseModel = new CategoryResponseModel(); 
		    	BeanUtils.copyProperties(categoryEntity, categoryResponseModel);
				
		    	if(returnValue.size() == 0) {
		    		categoryResponseModel.setTotalPage(totalPages);
		    	}
		    	
		    	returnValue.add(categoryResponseModel);
		    }
		return returnValue;
	}

	@Override
	public CategoryResponseModel getCategory(Integer categoryId) {		
		CategoryResponseModel returnValue = new CategoryResponseModel();
		CategoryEntity categoryEntity = categoryRepository.findByCategoryIdAndIsDeleted(categoryId,false);
		if(categoryEntity == null)
			throw new AppException("No category with this id");
		BeanUtils.copyProperties(categoryEntity, returnValue);
		return returnValue;
	}

	@Override
	public CategoryResponseModel updateCategory(Integer categoryId, CategoryRequestModel categoryDetail) {
		CategoryResponseModel returnValue = new CategoryResponseModel();
		CategoryEntity categoryEntity = categoryRepository.findByCategoryIdAndIsDeleted(categoryId, false);
		
		BeanUtils.copyProperties(categoryDetail, categoryEntity);
		CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
		BeanUtils.copyProperties(savedCategory, returnValue);
		return returnValue;
	}

	@Override
	public String deleteCategory(Integer categoryId) {
		CategoryEntity categoryEntity = categoryRepository.findByCategoryIdAndIsDeleted(categoryId,false);
		if(categoryEntity == null)
			throw new AppException("Invalid categoryId");
		categoryEntity.setDeleted(true);
		categoryRepository.save(categoryEntity);
		return "Category Deleted";
	}
	
	@Override
	public String importInventoryCategory(InventoryCategoryRequestModel importInventoryCategoryDetails) throws IOException {
		String returnValue = "";
		XSSFWorkbook workbook = new XSSFWorkbook(importInventoryCategoryDetails.getImportExcel().getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int i = 0;
		for (i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			
			XSSFRow row = worksheet.getRow(i);
			
			if (row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
				List<CategoryEntity> categoryEntities = categoryRepository.findByIsDeletedAndInventoryType(false,
				    row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				if (categoryEntities.size() == 0) {
					CategoryEntity categoryEntity = new CategoryEntity();
					categoryEntity.setInventoryType(row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
					categoryRepository.save(categoryEntity);
				}
			}
			
		}
		
		if (i > 1 && (worksheet.getPhysicalNumberOfRows()) > 1) {
			returnValue = "Inventory Items Imported Successfully";
		}
		
		return returnValue;
	}
	
}
