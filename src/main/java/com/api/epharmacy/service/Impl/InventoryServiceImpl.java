package com.api.epharmacy.service.Impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.epharmacy.exception.AppException;
import com.api.epharmacy.io.entity.CategoryEntity;
import com.api.epharmacy.io.entity.CustomerInventorySellPriceEntity;
import com.api.epharmacy.io.entity.CustomerInventoryTransactionEntity;
import com.api.epharmacy.io.entity.CustomerPurchasedInventoryEntity;
import com.api.epharmacy.io.entity.InventoryCostPriceListEntity;
import com.api.epharmacy.io.entity.InventoryEntity;
import com.api.epharmacy.io.entity.InventorySellPriceListEntity;
import com.api.epharmacy.io.entity.InventoryTransactionDetailEntity;
import com.api.epharmacy.io.repositories.CategoryRepository;
import com.api.epharmacy.io.repositories.CustomerInventorySellPriceRepository;
import com.api.epharmacy.io.repositories.CustomerInventoryTransactionRepository;
import com.api.epharmacy.io.repositories.CustomerPurchasedInventoryRepository;
import com.api.epharmacy.io.repositories.InventoryCostPriceListRepository;
import com.api.epharmacy.io.repositories.InventoryRepository;
import com.api.epharmacy.io.repositories.InventorySellPriceListRepository;
import com.api.epharmacy.io.repositories.InventoryTransactionDetailRepository;
import com.api.epharmacy.service.InventoryService;
import com.api.epharmacy.shared.GenerateRandomString;
import com.api.epharmacy.ui.model.request.ImportInventoryDataRequestModel;
import com.api.epharmacy.ui.model.request.ImportInventoryRequestModel;
import com.api.epharmacy.ui.model.request.InventoryCategoryRequestModel;
import com.api.epharmacy.ui.model.request.InventoryRequestModel;
import com.api.epharmacy.ui.model.request.SearchRequestModel;
import com.api.epharmacy.ui.model.response.InventoryCategoryResponse;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;
	
	@Autowired
	InventoryCostPriceListRepository inventoryCostPriceListRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	CustomerInventorySellPriceRepository customerInventorySellPriceRepository;
	
	@Value("${file.upload-dir}")
    private String uploadDirectory;
	
	@Autowired
	GenerateRandomString generateRandomString;
	
	@Autowired
	InventoryTransactionDetailRepository inventoryTransactionDetailRepository;
	
	@Autowired
	CustomerPurchasedInventoryRepository customerPurchasedInventoryRepository;
	
	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	@Override
	public InventoryRequestModel insertNewInventory(InventoryRequestModel inventoryDetails) throws IOException {
		
		if(inventoryRepository.findByInventoryGenericNameIgnoreCaseAndInventoryBrandNameIgnoreCaseAndInventoryTypeIgnoreCaseAndDosageFormIgnoreCaseAndStrengthIgnoreCase(inventoryDetails.getInventoryGenericName(), inventoryDetails.getInventoryBrandName(), inventoryDetails.getInventoryType(), inventoryDetails.getDosageForm(), inventoryDetails.getStrength()) != null)
			  throw new RuntimeException("Record already exists.");
		
		InventoryRequestModel returnvalue = new InventoryRequestModel();
		InventoryEntity inventoryEntity = new InventoryEntity();
		BeanUtils.copyProperties(inventoryDetails, inventoryEntity);
		
		if (inventoryDetails.getUploadImage() != null) {
//			String rootDirectory = new File("").getAbsolutePath();
			String rootDirectory = "/var/www/html/images";
			//			String rootDirectory = "C:/wamp64/www/images";
			
			String uploadDir = rootDirectory + "/inventory-item-images/";
			File directory = new File(uploadDir);
		    if (!directory.exists()){
		        directory.mkdirs();
		    }
					    
			byte[] bytes = inventoryDetails.getUploadImage().getBytes();
			Instant instant = Instant.now();
			long timeStampSeconds = instant.getEpochSecond();
			String randomText = generateRandomString.generateUserId(10) + "_" + timeStampSeconds;
			
			String fileName = inventoryDetails.getUploadImage().getOriginalFilename();
			String extention = fileName.substring(fileName.lastIndexOf(".") + 1);
			String newFileName = randomText + "." +  extention;
			
			Path path = Paths.get(uploadDir + newFileName);
		    Files.write(path, bytes);
		    
			inventoryEntity.setInventoryImage(newFileName);
		}
		
		InventoryEntity StoredInventoryDetail = inventoryRepository.save(inventoryEntity);
		
		BeanUtils.copyProperties(StoredInventoryDetail, returnvalue);
		
		return returnvalue;
	}

	@Override
	public InventoryRequestModel getInventoryByInventoryId(long inventoryId) {
		
		InventoryRequestModel returnvalue = new InventoryRequestModel();
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(inventoryId);
		
		if(inventoryEntity == null) throw new AppException("Inventory Item not found.");
		
		BeanUtils.copyProperties(inventoryEntity, returnvalue);
		
		return returnvalue;
	}

	@Override
	public List<InventoryRequestModel> getInventeryItems(String customerType, int page, int limit) {
		
		List<InventoryRequestModel> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	   
	    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("inventoryId").descending());
	    
	    Page<InventoryEntity> inventoryPage;
	    List<CustomerPurchasedInventoryEntity> customerPurchasedInventoryEntities = new ArrayList<>();
	    if("customer".equalsIgnoreCase(customerType)){
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    	customerPurchasedInventoryEntities = customerPurchasedInventoryRepository.findByCustomerIdAndIsDeleted(auth.getName(), false);	    	
			customerPurchasedInventoryEntities = customerPurchasedInventoryEntities.stream().filter(i->{
				return this.getCustomerAvailableQuantity(i) > 0;
		   }).collect(Collectors.toList());
			
	    	List<Long> inventoryIds = customerPurchasedInventoryEntities.stream().map(i->i.getInventoryId()).collect(Collectors.toList());
	    	inventoryPage = inventoryRepository.findByInventoryIdIn(inventoryIds,pageableRequest);	    	
	    } else {
		    inventoryPage = inventoryRepository.findAll(pageableRequest);
	    }

	    int totalPages = inventoryPage.getTotalPages();
	    List<InventoryEntity> inventoryItems = inventoryPage.getContent();
	    for(InventoryEntity inventoryEntity : inventoryItems) {
	    	//int size = returnValue.size();
	    	InventoryRequestModel inventoryRequestModel = new InventoryRequestModel(); 
	    	BeanUtils.copyProperties(inventoryEntity, inventoryRequestModel);
	    	if("customer".equalsIgnoreCase(customerType)){
		    	Optional<CustomerPurchasedInventoryEntity> customerPurchasedInventoryEntity = customerPurchasedInventoryEntities.stream().filter(cpi->cpi.getInventoryId() == inventoryEntity.getInventoryId()).findFirst();
		    	if(customerPurchasedInventoryEntity.isPresent()) {
		    		inventoryRequestModel.setAvailableQuantity(this.getCustomerAvailableQuantity(customerPurchasedInventoryEntity.get()));
			    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    		CustomerInventorySellPriceEntity customerInventorySellPriceEntity=customerInventorySellPriceRepository.findTopByInventoryIdAndCustomerIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(inventoryEntity.getInventoryId(), auth.getName(), Instant.now(), false);
		    		if(customerInventorySellPriceEntity!=null) {
		    			inventoryRequestModel.setCurrentPrice(customerInventorySellPriceEntity.getSellPrice());
		    		}
		    	}
		    }
	    	if(returnValue.size() == 0) {
	    		inventoryRequestModel.setTotalPages(totalPages);
	    	}
	    	returnValue.add(inventoryRequestModel);
	    }
	    
		return returnValue;
		
	}

	@Override
	public InventoryRequestModel updateInventoryItem(long inventoryId, InventoryRequestModel inventoryItem) throws IOException {
		
		InventoryRequestModel returnValue = new InventoryRequestModel();
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(inventoryId);
		
		if(inventoryEntity == null) 
			throw new RuntimeException("Inventory Item not found.");
		
		inventoryEntity.setInventoryGenericName(inventoryItem.getInventoryGenericName());
		inventoryEntity.setInventoryBrandName(inventoryItem.getInventoryBrandName());
		inventoryEntity.setInventoryType(inventoryItem.getInventoryType());
		inventoryEntity.setMeasuringUnit(inventoryItem.getMeasuringUnit());
		inventoryEntity.setMinimumStockQuantity(inventoryItem.getMinimumStockQuantity());
		inventoryEntity.setSubCategory(inventoryItem.getSubCategory());
		inventoryEntity.setDosageForm(inventoryItem.getDosageForm());
		inventoryEntity.setStrength(inventoryItem.getStrength());
		inventoryEntity.setVolume(inventoryItem.getVolume());
		
		if(inventoryItem.getUploadImage() != null) {
			
			//String rootDirectory = new File("").getAbsolutePath();
			String rootDirectory = "/var/www/html/images";
			//			String rootDirectory = "C:/wamp64/www/images";
			String uploadDir = rootDirectory + "/inventory-item-images/";
			File directory = new File(uploadDir);
		    if (!directory.exists()){
		        directory.mkdirs();
		    }
		    
			byte[] bytes = inventoryItem.getUploadImage().getBytes();
			Instant instant = Instant.now();
			long timeStampSeconds = instant.getEpochSecond();
			String randomText = generateRandomString.generateUserId(10) + "_" + timeStampSeconds;
			
			String fileName = inventoryItem.getUploadImage().getOriginalFilename();
			String extention = fileName.substring(fileName.lastIndexOf(".") + 1);
			String newFileName = randomText + "." +  extention;
			
			Path path = Paths.get(uploadDir + newFileName);
		    Files.write(path, bytes);
		    
			inventoryEntity.setInventoryImage(newFileName);
		}
		
		InventoryEntity updatedInventoryItem = inventoryRepository.save(inventoryEntity);
		
		BeanUtils.copyProperties(updatedInventoryItem, returnValue); 
		returnValue.setInventoryId(inventoryEntity.getInventoryId());
		return returnValue;
	}

	@Override
	public List<InventoryRequestModel> searchInventeryItems(SearchRequestModel searchkeyDetail, int page, int limit) {
		
		String searchKey = searchkeyDetail.getSearchKey();
		List<InventoryRequestModel> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	   
	    Pageable pageableRequest = PageRequest.of(page, limit);
	    Page<InventoryEntity> inventoryPage;
	    List<CustomerPurchasedInventoryEntity> customerPurchasedInventoryEntities = new ArrayList<>();
	    if("customer".equalsIgnoreCase(searchkeyDetail.getCustomerType())){
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    	customerPurchasedInventoryEntities = customerPurchasedInventoryRepository.findByCustomerIdAndIsDeleted(auth.getName(), false);
	    	List<Long> inventoryIds = customerPurchasedInventoryEntities.stream().map(i->i.getInventoryId()).collect(Collectors.toList());
	    	inventoryPage = inventoryRepository.findByInventoryIdInAndInventoryGenericNameContainingOrInventoryIdInAndInventoryBrandNameContainingOrInventoryIdInAndInventoryTypeContainingOrInventoryIdInAndMeasuringUnitContaining(inventoryIds, searchKey,inventoryIds,searchKey,inventoryIds,searchKey,inventoryIds,searchKey,pageableRequest);	    	
	    } else {
	    	inventoryPage = inventoryRepository.findByInventoryGenericNameContainingOrInventoryBrandNameContainingOrInventoryTypeContainingOrMeasuringUnitContaining(searchKey,searchKey,searchKey,searchKey,pageableRequest);
	    }
	    int totalPages = inventoryPage.getTotalPages();
	    List<InventoryEntity> inventoryItems = inventoryPage.getContent();
	    for(InventoryEntity inventoryEntity : inventoryItems) {
	    	//int size = returnValue.size();
	    	InventoryRequestModel inventoryRequestModel = new InventoryRequestModel(); 
	    	BeanUtils.copyProperties(inventoryEntity, inventoryRequestModel);
		    if("customer".equalsIgnoreCase(searchkeyDetail.getCustomerType())){
		    	Optional<CustomerPurchasedInventoryEntity> customerPurchasedInventoryEntity = customerPurchasedInventoryEntities.stream().filter(cpi->cpi.getInventoryId() == inventoryEntity.getInventoryId()).findFirst();
		    	if(customerPurchasedInventoryEntity.isPresent()) {
		    		inventoryRequestModel.setAvailableQuantity(this.getCustomerAvailableQuantity(customerPurchasedInventoryEntity.get()));
		    		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    		CustomerInventorySellPriceEntity customerInventorySellPriceEntity=customerInventorySellPriceRepository.findTopByInventoryIdAndCustomerIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(inventoryEntity.getInventoryId(), auth.getName(), Instant.now(), false);
		    		if(customerInventorySellPriceEntity!=null) {
		    			inventoryRequestModel.setCurrentPrice(customerInventorySellPriceEntity.getSellPrice());
		    		}
		    	}
		    }
	    	if(returnValue.size() == 0) {
	    		inventoryRequestModel.setTotalPages(totalPages);
	    	}
	    	returnValue.add(inventoryRequestModel);
	    }
	    
		return returnValue;
	}

	@Override
	public List<InventoryCategoryResponse> getInventoryCategories() {
		
		List<InventoryCategoryResponse> returnValue = new ArrayList<>();
		List<String> categories = inventoryRepository.findDistinctInventoryType();
		
		for(String category : categories) {
			InventoryCategoryResponse categoryResponse = new InventoryCategoryResponse(); 
	    	categoryResponse.setInventoryType(category);
	    	returnValue.add(categoryResponse);
	    }
		
		return returnValue;
	}

	@Override
	public String importInventoryItems(ImportInventoryRequestModel importInventoryDetails) throws IOException {
		
		String returnValue = "";
		XSSFWorkbook workbook = new XSSFWorkbook(importInventoryDetails.getImportExcel().getInputStream());
	    XSSFSheet worksheet = workbook.getSheetAt(0);
	    int i;
		for (i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

	    	InventoryEntity inventoryEntity = new InventoryEntity();
	    	
	        XSSFRow row = worksheet.getRow(i);
	        
			//	        String genericName = row.getCell(0,Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			//	        String brandName = row.getCell(6,Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			//	        String inventoryType = row.getCell(4,Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			//	        String dosageForm = row.getCell(1,Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			//	        String strength = row.getCell(2,Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			//	        
			//	      
			//	        Pageable pageableRequest = PageRequest.of(1, 1);
			//			Page<InventoryEntity> checkItem = inventoryRepository.findByInventoryGenericNameIgnoreCaseAndInventoryBrandNameIgnoreCaseAndInventoryTypeIgnoreCaseAndDosageFormIgnoreCaseAndStrengthIgnoreCase(genericName, brandName, inventoryType, dosageForm, strength,pageableRequest);
			//			long count = checkItem.getTotalElements();
			//	        if(count == 0) {
			
			//-----------------------------------------Save Inventory Items----------------------------------------------------------------
	        	if(row.getCell(0,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
		        	inventoryEntity.setInventoryGenericName(row.getCell(0).getStringCellValue());
		        }
		        
		        if(row.getCell(1,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
		        	inventoryEntity.setDosageForm(row.getCell(1).getStringCellValue());
		        }
		        
		        if(row.getCell(2,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
		        	inventoryEntity.setStrength(row.getCell(2).getStringCellValue());
		        }
		        
		        if(row.getCell(3,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
		        	inventoryEntity.setVolume(row.getCell(3).getStringCellValue());
		        }
		        
		        if(row.getCell(4,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
		        	inventoryEntity.setInventoryType(row.getCell(4).getStringCellValue());
		        }
		        
		        if(row.getCell(5,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
		        	inventoryEntity.setSubCategory(row.getCell(5).getStringCellValue());
		        }
		        
		        if(row.getCell(6,Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
		        	inventoryEntity.setInventoryBrandName(row.getCell(6).getStringCellValue());
		        }
		        
			if (row.getCell(7, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventoryEntity.setMinimumStockQuantity((float) row.getCell(7).getNumericCellValue());
			}
		        
			if (row.getCell(8, Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
				inventoryEntity.setMeasuringUnit(row.getCell(8).getStringCellValue());
			}
			
			if (row.getCell(12, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventoryEntity.setAvailableQuantity((float) row.getCell(12).getNumericCellValue());
			}
		        
			InventoryEntity savedDetail = inventoryRepository.save(inventoryEntity);
			
			//-----------------------------------Save Inventory Sell Price List---------------------------------------------------			
			InventorySellPriceListEntity inventorySellPriceListEntity = new InventorySellPriceListEntity();
			if (row.getCell(9, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventorySellPriceListEntity.setSellPrice((float) row.getCell(9).getNumericCellValue());
			}
			
			if (row.getCell(11, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventorySellPriceListEntity.setDiscountAmount((float) row.getCell(11).getNumericCellValue());
			}
			
			inventorySellPriceListEntity.setEffectiveDateTime(Instant.now());
			inventorySellPriceListEntity.setNowEffectiveDateTimeDifference(0);
			inventorySellPriceListEntity.setInventoryId(savedDetail.getInventoryId());
			inventorySellPriceListRepository.save(inventorySellPriceListEntity);
			
			//-----------------------------------Save Inventory Cost Price List---------------------------------------------------			
			InventoryCostPriceListEntity inventoryCostPriceListEntity = new InventoryCostPriceListEntity();
			if (row.getCell(10, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventoryCostPriceListEntity.setCostOfInventory((float) row.getCell(10).getNumericCellValue());
			}
			
			if (row.getCell(12, Row.CREATE_NULL_AS_BLANK).getNumericCellValue() >= 0) {
				inventoryCostPriceListEntity.setQuantity((float) row.getCell(12).getNumericCellValue());
			}
			
			inventoryCostPriceListEntity.setCostDateTime(Instant.now());
			inventoryCostPriceListEntity.setInventoryId(savedDetail.getInventoryId());
			inventoryCostPriceListRepository.save(inventoryCostPriceListEntity);
			
			InventoryTransactionDetailEntity inventoryTransactionDetailEntity = new InventoryTransactionDetailEntity();
			inventoryTransactionDetailEntity.setCostOfInventory(inventoryCostPriceListEntity.getCostOfInventory());
			inventoryTransactionDetailEntity.setDiscountAmount(inventorySellPriceListEntity.getDiscountAmount());
			inventoryTransactionDetailEntity.setQuantity(inventoryCostPriceListEntity.getQuantity());
			inventoryTransactionDetailEntity.setInventoryId(savedDetail.getInventoryId());
			inventoryTransactionDetailEntity.setTransactionTime(Instant.now());
			inventoryTransactionDetailEntity.setTransactionType("In");
			inventoryTransactionDetailRepository.save(inventoryTransactionDetailEntity);
			
			if (row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue() != null) {
				List<CategoryEntity> categoryEntities = categoryRepository.findByIsDeletedAndInventoryType(false,
				    row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				if (categoryEntities.size() == 0) {
					CategoryEntity categoryEntity = new CategoryEntity();
					categoryEntity.setInventoryType(row.getCell(4).getStringCellValue());
					categoryRepository.save(categoryEntity);
				}
			}
			
	        }
	        
	          
		if (i > 1 && (worksheet.getPhysicalNumberOfRows()) > 1) {
			returnValue = "Inventory Items Imported Successfully";
		}
		
		return returnValue;
	}

	@Override
	public List<InventoryRequestModel> getSelectedInventeryItems(Long[] inventoryIds) {
		List<InventoryRequestModel> returnValue = new ArrayList<>(); 

		List<InventoryEntity> inventoryItems = inventoryRepository.findByInventoryIdIn(inventoryIds);
	    for(InventoryEntity inventoryEntity : inventoryItems) {
	    	InventoryRequestModel inventoryRequestModel = new InventoryRequestModel(); 
	    	BeanUtils.copyProperties(inventoryEntity, inventoryRequestModel);
	    	returnValue.add(inventoryRequestModel);
	    }
	    
		return returnValue;
	}
	@Override
	public String importPatientData(ImportInventoryDataRequestModel requestDetail) throws IOException {

		String returnValue = "";

		XSSFWorkbook workbook = new XSSFWorkbook(requestDetail.getImportExcel().getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter(Locale.US);
		int i;
		for (i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String brandName = formatter.formatCellValue(row.getCell(0));
			formatter.formatCellValue(row.getCell(0));
			formatter.formatCellValue(row.getCell(0));
			formatter.formatCellValue(row.getCell(0));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			Double.valueOf(formatter.formatCellValue(row.getCell(5)));
			
			System.out.println("brandName "+brandName);
			//			CategoryEntity categoryEntity = categoryRepository.findByInventoryTypeAndIsDeleted(brandName, false);
			//			if(categoryEntity == null) {
			//			 categoryEntity = new CategoryEntity();
			//			 categoryEntity.setInventoryType(category);
			//			 categoryEntity = categoryRepository.save(categoryEntity);
			//			}
		}
			
		return returnValue;
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
				if (categoryEntities.size() > 0)
					throw new AppException("Record already exist!");
				
				CategoryEntity categoryEntity = new CategoryEntity();
				categoryEntity.setInventoryType(row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				categoryRepository.save(categoryEntity);
				
			}
			
			
		}
		
		if (i > 1 && (worksheet.getPhysicalNumberOfRows()) > 1) {
			returnValue = "Inventory Items Imported Successfully";
		}
		
		return returnValue;
	}
	
	private double getCustomerAvailableQuantity(CustomerPurchasedInventoryEntity i) {
		double inQuantity=0;
		double outQuantity =0;
		List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(i.getInventoryId(), false);
	   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity: customerInventoryTransactionEntities) {   
		   if("In".equalsIgnoreCase(customerInventoryTransactionEntity.getTransactionType())) {
				  inQuantity += customerInventoryTransactionEntity.getQuantity();
		   }
		   else if("Out".equalsIgnoreCase(customerInventoryTransactionEntity.getTransactionType())) {
			  outQuantity += customerInventoryTransactionEntity.getQuantity();
		   }           
	   }
	   return inQuantity - outQuantity;
	}
}
