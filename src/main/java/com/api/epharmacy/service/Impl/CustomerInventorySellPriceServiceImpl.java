package com.api.epharmacy.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.apache.xmlbeans.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.api.epharmacy.exception.AppException;
import com.api.epharmacy.io.entity.CustomerInventorySellPriceEntity;
import com.api.epharmacy.io.entity.CustomerInventoryTransactionEntity;
import com.api.epharmacy.io.entity.InventoryEntity;
import com.api.epharmacy.io.entity.UserEntity;
import com.api.epharmacy.io.repositories.CustomerInventorySellPriceRepository;
import com.api.epharmacy.io.repositories.CustomerInventoryTransactionRepository;
import com.api.epharmacy.io.repositories.InventoryRepository;
import com.api.epharmacy.io.repositories.UserRepository;
import com.api.epharmacy.service.CustomerInventorySellPriceService;
import com.api.epharmacy.ui.model.request.CustomerInventorySellPriceRequestModel;
import com.api.epharmacy.ui.model.response.CustomerInventorySellPriceResponseModel;

@Transactional
@Service
public class CustomerInventorySellPriceServiceImpl implements CustomerInventorySellPriceService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	CustomerInventorySellPriceRepository customerInventorySellPriceRepository;
	
	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	@Override
	public String deleteCustomerInventorySellPrice(long customerInventorySellPriceId) {
		// TODO Auto-generated method stub
		CustomerInventorySellPriceEntity customerInventorySellPriceEntity = customerInventorySellPriceRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(customerInventorySellPriceId,Instant.now(), false);
		if(customerInventorySellPriceEntity==null) throw new AppException("no data with this id");
		customerInventorySellPriceEntity.setDeleted(true);
		customerInventorySellPriceRepository.save(customerInventorySellPriceEntity);
		return "Deleted!";
	}

	@Override
	public CustomerInventorySellPriceResponseModel updateCustomerInventorySellPrice(long customerInventorySellPriceId,
			CustomerInventorySellPriceRequestModel customerInventorySellPriceDetail) {
		// TODO Auto-generated method stub
		CustomerInventorySellPriceEntity customerInventorySellPriceEntity = customerInventorySellPriceRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(customerInventorySellPriceId, Instant.now(), false);
		if(customerInventorySellPriceEntity==null) throw new AppException("no data with this id");
		CustomerInventorySellPriceResponseModel returnValue = new CustomerInventorySellPriceResponseModel();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        customerInventorySellPriceEntity.setCustomerId(auth.getName());
		BeanUtils.copyProperties(customerInventorySellPriceDetail, customerInventorySellPriceEntity);
		CustomerInventorySellPriceEntity savedCustomerInventorySellPriceEntity = customerInventorySellPriceRepository.save(customerInventorySellPriceEntity);
		BeanUtils.copyProperties(savedCustomerInventorySellPriceEntity, returnValue);
		return returnValue;
	}

	@Override
	public CustomerInventorySellPriceResponseModel getCustomerInventorySellPrice(long customerInventorySellPriceId) {
		// TODO Auto-generated method stub         
		CustomerInventorySellPriceResponseModel returnValue = new CustomerInventorySellPriceResponseModel();
		CustomerInventorySellPriceEntity customerInventorySellPriceEntity = customerInventorySellPriceRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(customerInventorySellPriceId, Instant.now(), false);
		if(customerInventorySellPriceEntity==null) throw new AppException("no data with this id");
		 List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerInventorySellPriceEntity.getInventoryId(), false);
		   double availableQuantity=0;
		   double outQuantity =0;
		   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity1:customerInventoryTransactionEntities) {   
			   if(customerInventoryTransactionEntity1.getTransactionType()=="In") {
				  availableQuantity=availableQuantity+ customerInventoryTransactionEntity1.getQuantity();
			   }
			   else if(customerInventoryTransactionEntity1.getTransactionType()=="Out") {
				   outQuantity = outQuantity+customerInventoryTransactionEntity1.getQuantity();
			   }                
		   }
		   double difference=availableQuantity-outQuantity;
		   returnValue.setQuantity(difference);
		   UserEntity userEntity = userRepository.findByUserId(customerInventorySellPriceEntity.getCustomerId());
		   if(userEntity!=null) {
			   returnValue.setCustomer(userEntity.getFirstName()+" "+userEntity.getLastName()+" "+userEntity.getGrandFatherName());
		   }
		   InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(customerInventorySellPriceEntity.getInventoryId(), false);
		   if(inventoryEntity!=null) {
			   returnValue.setInventoryName(inventoryEntity.getInventoryGenericName()+" "+inventoryEntity.getInventoryBrandName()+" "+inventoryEntity.getDosageForm());   
		   }
		BeanUtils.copyProperties(customerInventorySellPriceEntity, returnValue);
		return returnValue;
	}

	@Override
	public List<CustomerInventorySellPriceResponseModel> getAllCustomerInventorySellPrices(int page, int limit,
			long inventoryId, long companyId) {
		
		// TODO Auto-generated method stub
		List<CustomerInventorySellPriceResponseModel> returnValue = new ArrayList<>();
		 if(page > 0) page = page - 1;   
		    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("customerInventorySellPriceId").ascending());
		    Page<CustomerInventorySellPriceEntity> customerInventorySellPricePage = null;    
//		    if("".equals(searchKey))
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    UserEntity userEntity1 = userRepository.findByUserId(auth.getName());
		    String userType = null;
		    if(userEntity1!=null)
		    	userType = userEntity1.getUserType();
		    if("Admin".equals(userType)) {
		    	if(companyId!=0)
		    		customerInventorySellPricePage = customerInventorySellPriceRepository.findByCompanyIdAndInventoryIdAndIsDeleted(companyId, inventoryId, false, pageableRequest);
		    	else 
		    		customerInventorySellPricePage = customerInventorySellPriceRepository.findByInventoryIdAndIsDeleted(inventoryId,false,pageableRequest);
		    }
		    else if("Customer".equals(userType)|| "CustomerAdmin".equals(userType))
		    	customerInventorySellPricePage = customerInventorySellPriceRepository.findByCompanyIdAndInventoryIdAndIsDeleted(userEntity1.getCompanyId(), inventoryId, false, pageableRequest);
		    
		    customerInventorySellPricePage = customerInventorySellPriceRepository.findByCustomerIdAndInventoryIdAndIsDeleted(auth.getName(), inventoryId, false, pageableRequest);//.findAll(pageableRequest);
	    	CustomerInventorySellPriceEntity price = customerInventorySellPriceRepository.findTopByInventoryIdAndCustomerIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(inventoryId,auth.getName(), Instant.now(), false);						
//		    else
//		    	customerInventoryTransactionPage = countryRepository.findByIsDeletedAndNameContaining(false,searchKey, pageableRequest);//.findAll(pageableRequest);
		    List<CustomerInventorySellPriceEntity> customerInventorySellPriceEntities = customerInventorySellPricePage.getContent();  
		    int totalPages = customerInventorySellPricePage.getTotalPages();	    
		    for(CustomerInventorySellPriceEntity customerInventorySellPriceEntity : customerInventorySellPriceEntities) {
		    	CustomerInventorySellPriceResponseModel customerInventorySellPriceResponseModel = new CustomerInventorySellPriceResponseModel(); 
		    	BeanUtils.copyProperties(customerInventorySellPriceEntity, customerInventorySellPriceResponseModel); 
				 List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerInventorySellPriceEntity.getInventoryId(), false);
				   double availableQuantity=0;
				   double outQuantity =0;
				   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity1:customerInventoryTransactionEntities) {   
					   if(customerInventoryTransactionEntity1.getTransactionType()=="In") {
						  availableQuantity=availableQuantity+ customerInventoryTransactionEntity1.getQuantity();
					   }
					   else if(customerInventoryTransactionEntity1.getTransactionType()=="Out") {
						   outQuantity = outQuantity+customerInventoryTransactionEntity1.getQuantity();
					   }                
				   }
				   double difference=availableQuantity-outQuantity;
				   customerInventorySellPriceResponseModel.setQuantity(difference);
				 if(price.getCustomerInventorySellPriceId()==customerInventorySellPriceEntity.getCustomerInventorySellPriceId()) {
					 customerInventorySellPriceResponseModel.setActiveSellPrice(true);;
				 }
		    	UserEntity userEntity= userRepository.findByUserId(customerInventorySellPriceEntity.getCustomerId());
				if(userEntity!=null)
					customerInventorySellPriceResponseModel.setCustomer(userEntity.getFirstName()+" "+userEntity.getLastName());
		    	else
		    		customerInventorySellPriceResponseModel.setCustomer("");
				InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(customerInventorySellPriceEntity.getInventoryId(), false);
				if(inventoryEntity!=null) {
					customerInventorySellPriceResponseModel.setInventoryName(inventoryEntity.getInventoryBrandName()+" "+inventoryEntity.getInventoryGenericName());
				}
		    	if(returnValue.size() == 0) {
		    		customerInventorySellPriceResponseModel.setTotalPages(totalPages);
		    	}
		    	returnValue.add(customerInventorySellPriceResponseModel);
		    }
		return returnValue;
	}

	@Override
	public CustomerInventorySellPriceResponseModel saveCustomerInventorySelllPrice(
			CustomerInventorySellPriceRequestModel customerInventorySellPriceDetail) {
		// TODO Auto-generated method stub
		
		CustomerInventorySellPriceEntity customerInventorySellPriceEntity = new CustomerInventorySellPriceEntity();
		CustomerInventorySellPriceResponseModel returnValue = new CustomerInventorySellPriceResponseModel();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        customerInventorySellPriceEntity.setCustomerId(customerInventorySellPriceDetail.getCustomerId());
		UserEntity userEntity = userRepository.findByUserId(auth.getName());
		BeanUtils.copyProperties(customerInventorySellPriceDetail, customerInventorySellPriceEntity);
		if(userEntity!=null) {
			customerInventorySellPriceEntity.setCompanyId(userEntity.getCompanyId());
			UserEntity userEntity2 = userRepository.findTopByCompanyIdAndUserTypeAndIsDeleted(userEntity.getCompanyId(), "Customer",false);
			if (userEntity2!=null) {
				customerInventorySellPriceEntity.setCustomerId(userEntity2.getUserId());
			}
		}
		customerInventorySellPriceEntity.setCreatedBy(auth.getName());
		CustomerInventorySellPriceEntity savedCustomerInventorySellPriceEntity = customerInventorySellPriceRepository.save(customerInventorySellPriceEntity);
		BeanUtils.copyProperties(savedCustomerInventorySellPriceEntity, returnValue);
		return returnValue;
	}
}
