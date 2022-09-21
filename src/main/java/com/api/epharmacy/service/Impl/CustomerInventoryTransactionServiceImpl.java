package com.api.epharmacy.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
import com.api.epharmacy.io.entity.CustomerInventoryTransactionEntity;
import com.api.epharmacy.io.entity.InventoryEntity;
import com.api.epharmacy.io.entity.TransactionReasonEntity;
import com.api.epharmacy.io.entity.UserEntity;
import com.api.epharmacy.io.repositories.CustomerInventoryTransactionRepository;
import com.api.epharmacy.io.repositories.InventoryRepository;
import com.api.epharmacy.io.repositories.TransactionReasonRepository;
import com.api.epharmacy.io.repositories.UserRepository;
import com.api.epharmacy.service.CustomerInventoryTransactionService;
import com.api.epharmacy.ui.model.request.CustomerInventoryTransactionRequestModel;
import com.api.epharmacy.ui.model.response.CustomerInventoryTransactionResponseModel;

@Service
@Transactional
public class CustomerInventoryTransactionServiceImpl implements CustomerInventoryTransactionService {

	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	TransactionReasonRepository transactionReasonRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Override
	public CustomerInventoryTransactionResponseModel saveCustomerInventoryTransaction(
			CustomerInventoryTransactionRequestModel customerInventoryTransactionDetail) {
		// TODO Auto-generated method stub
		   List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerInventoryTransactionDetail.getInventoryId(), false);
		   double availableQuantity=0;
		   double outQuantity =0;
		   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity:customerInventoryTransactionEntities) {   
			   if(customerInventoryTransactionEntity.getTransactionType()=="In") {
				  availableQuantity=availableQuantity+ customerInventoryTransactionEntity.getQuantity();
			   }
			   else if(customerInventoryTransactionEntity.getTransactionType()=="Out") {
				   outQuantity = outQuantity+customerInventoryTransactionEntity.getQuantity();
			   }                
		   }
		   double difference=availableQuantity-outQuantity;
		   if(customerInventoryTransactionDetail.getTransactionType().equals("Out")&& difference<customerInventoryTransactionDetail.getQuantity()) {
			   return null;
		   }

		CustomerInventoryTransactionResponseModel returnValue = new CustomerInventoryTransactionResponseModel();
		CustomerInventoryTransactionEntity customerInventoryTransactionEntity = new CustomerInventoryTransactionEntity();
		BeanUtils.copyProperties(customerInventoryTransactionDetail, customerInventoryTransactionEntity);
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserEntity userEntity = userRepository.findByUserId(auth.getName());
	    if(userEntity!=null) {
	    	customerInventoryTransactionEntity.setCompanyId(userEntity.getCompanyId());
	    	UserEntity userEntity2 = userRepository.findTopByCompanyIdAndUserTypeAndIsDeleted(userEntity.getCompanyId(), "Customer", false);
	    	if(userEntity2!=null)
	    		customerInventoryTransactionEntity.setCustomerId(userEntity2.getUserId());
	    }
	    customerInventoryTransactionEntity.setCreatedBy(auth.getName());
		CustomerInventoryTransactionEntity savedCustomerInventoryTransactionEntity = customerInventoryTransactionRepository.save(customerInventoryTransactionEntity);
		BeanUtils.copyProperties(savedCustomerInventoryTransactionEntity, returnValue);
		return returnValue;
	}

	@Override
	public List<CustomerInventoryTransactionResponseModel> getAllCustomerInventoryTransactions(int page, int limit,
			String searchKey, long companyId) {
		// TODO Auto-generated method stub
		List<CustomerInventoryTransactionResponseModel> returnValue = new ArrayList<>();
		 if(page > 0) page = page - 1; 
		    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("customerInventoryTransactionId").descending());
		    Page<CustomerInventoryTransactionEntity> customerInventoryTransactionPage = null;
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String customerId = auth.getName();
		    String userType = null;
		    UserEntity userEntity1 = userRepository.findByUserId(auth.getName());
		    if(userEntity1!=null)
		    	userType = userEntity1.getUserType();
		    if("Admin".equals(userType)) {
		    	if(companyId!=0)
		    		customerInventoryTransactionPage = customerInventoryTransactionRepository.findByCompanyIdAndIsDeleted(companyId, false, pageableRequest);
		        else
		        	customerInventoryTransactionPage = customerInventoryTransactionRepository.findByIsDeleted(false, pageableRequest);
		    }
		    else if("Customer".equals(userType)||"CustomerAdmin".equals(userType))
		    	customerInventoryTransactionPage = customerInventoryTransactionRepository.findByCompanyIdAndIsDeleted(userEntity1.getCompanyId(), false,pageableRequest);
		    else {
		    	customerInventoryTransactionPage = customerInventoryTransactionRepository.findByCompanyIdAndCreatedByAndIsDeleted(userEntity1.getCompanyId(),auth.getName(), false, pageableRequest);//.findAll(pageableRequest);
			}

			List<Object[]> results = new ArrayList<Object[]>();
			long totalRows = 0;
           if(!"".equals(searchKey)) {
//			results = entityManager.createQuery(
//				    "SELECT i.inventoryId, i.inventoryBrandName, i.inventoryGenericName, i.dosageForm, t.quantity, t.transactionReasonId,"
//				    + "t.transactionType, t.customerId, t.transactionDate from inventory i, customer_inventory_transaction t"
//				    + "where t.inventoryId = i.inventoryId AND t.isDeleted=0 t.customerId<=:customerId "
//				    + "AND (i.inventoryBrandName LIKE :searchKey OR i.inventoryGenericName LIKE :searchKey OR i.dosageForm LIKE :searchKey) "
//				    + "order by t.customerInventoryTransactionId desc",
//				    Object[].class)
//				        .setParameter("customerId", customerId)
//				        .setParameter("searchKey", '%' + searchKey + '%').setFirstResult((page) * limit).setMaxResults(limit)
//				        .getResultList();
        	   results = entityManager.createQuery(
   				    "SELECT i.inventoryId, i.inventoryBrandName, i.inventoryGenericName, i.dosageForm, t.quantity, t.transactionReasonId, t.transactionType, t.customerId, t.transactionDate from customer_inventory_transaction t, inventory i where i.inventoryId = t.inventoryId AND t.customerId=:customerId"
   				    + " AND (i.inventoryBrandName LIKE :searchKey OR i.inventoryGenericName LIKE :searchKey OR i.dosageForm LIKE :searchKey)", Object[].class)
   				        .setParameter("customerId", customerId).setParameter("searchKey", '%' + searchKey + '%').setFirstResult((page) * limit).setMaxResults(limit).getResultList();
              
			totalRows = (long) entityManager.createQuery(
				    "SELECT count(*) from customer_inventory_transaction t, inventory i where i.inventoryId = t.inventoryId AND t.customerId=:customerId"
				    + " AND (i.inventoryBrandName LIKE :searchKey OR i.inventoryGenericName LIKE :searchKey OR i.dosageForm LIKE :searchKey)")
				        .setParameter("customerId", customerId).setParameter("searchKey", '%' + searchKey + '%').getSingleResult();
           }
           else {
        	   results = entityManager.createQuery(
      				    "SELECT i.inventoryId, i.inventoryBrandName, i.inventoryGenericName, i.dosageForm, t.quantity, t.transactionReasonId, t.transactionType, t.customerId, t.transactionDate from customer_inventory_transaction t, inventory i where i.inventoryId = t.inventoryId AND t.customerId=:customerId", Object[].class)
      				        .setParameter("customerId", customerId).setFirstResult((page) * limit).setMaxResults(limit).getResultList();
                 
   			totalRows = (long) entityManager.createQuery(
   				    "SELECT count(*) from customer_inventory_transaction t, inventory i where i.inventoryId = t.inventoryId AND t.customerId=:customerId")
   				        .setParameter("customerId", customerId).getSingleResult();
//        	   results = entityManager.createQuery(
//   				    "SELECT i.inventoryId, i.inventoryBrandName, i.inventoryGenericName, i.dosageForm, t.quantity, t.transactionReasonId,"
//   				    + "t.transactionType, t.customerId, t.transactionDate from inventory i, customer_inventory_transaction t "
//   				    + "where t.inventoryId = i.inventoryId AND t.isDeleted=0 t.customerId<=:customerId order by t.customerInventoryTransactionId desc",
//   				    Object[].class)
//   				        .setParameter("customerId", customerId).setFirstResult((page) * limit).setMaxResults(limit)
//   				        .getResultList();
//   			totalRows = (long) entityManager.createQuery(
//   				    "SELECT count(*) from customer_inventory_transaction t, inventory i where i.inventoryId = t.inventoryId AND t.customerId=:customerId"
//   				    + "order by p.patientId desc")
//   				        .setParameter("customerId", customerId).getSingleResult();
           }
//				if("".equals(searchKey))
//		    else
//		    	customerInventoryTransactionPage = countryRepository.findByIsDeletedAndNameContaining(false,searchKey, pageableRequest);//.findAll(pageableRequest);
		 
		    List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionPage.getContent();
//		    int totalPages = customerInventoryTransactionPage.getTotalPages();	    
		    for(Object[] result : results) {

		    	CustomerInventoryTransactionResponseModel customerInventoryTransactionResponseModel = new CustomerInventoryTransactionResponseModel(); 
		    	customerInventoryTransactionResponseModel.setInventoryName((String) result[1] + (String) result[2] + (String) result[3]);
		    	customerInventoryTransactionResponseModel.setInventoryId((long) result[0]);
		    	customerInventoryTransactionResponseModel.setCustomerId((String) result[7]);
		    	customerInventoryTransactionResponseModel.setQuantity((double) result[4]);
		    	customerInventoryTransactionResponseModel.setTransactionDate((Instant) result[8]);
		    	customerInventoryTransactionResponseModel.setTransactionReasonId((Integer) result[5]);
		    	customerInventoryTransactionResponseModel.setTransactionType((String) result[6]);
		    	
//		    	BeanUtils.copyProperties(customerInventoryTransactionEntity, customerInventoryTransactionResponseModel);
		    	
		    	TransactionReasonEntity transactionReasonEntity = transactionReasonRepository.findByTransactionReasonIdAndIsDeleted((Integer) result[5], false);
		    	if(transactionReasonEntity!=null) {
		    		BeanUtils.copyProperties(transactionReasonEntity, customerInventoryTransactionResponseModel);
		    		customerInventoryTransactionResponseModel.setTransactionReasonId(transactionReasonEntity.getTransactionReasonId());
		    		customerInventoryTransactionResponseModel.setTransactionReason(transactionReasonEntity.getTransactionReason());
		    	}
		    	UserEntity userEntity= userRepository.findByUserId((String) result[7]);
				if(userEntity!=null)
					customerInventoryTransactionResponseModel.setCustomer(userEntity.getFirstName()+" "+userEntity.getLastName());
		    	else
		    		customerInventoryTransactionResponseModel.setCustomer("");
				if (returnValue.size() == 0) {
					if (totalRows % limit == 0)
						customerInventoryTransactionResponseModel.setTotalPages(totalRows / limit);
					else {
						customerInventoryTransactionResponseModel.setTotalPages((totalRows / limit) + 1);
					}
				}
		    	
		    	returnValue.add(customerInventoryTransactionResponseModel);
		    }
		return returnValue;
	}

	@Override
	public CustomerInventoryTransactionResponseModel getCustomerInventoryTransaction(
			long customerInventoryTransactionId) {
		// TODO Auto-generated method stub
		CustomerInventoryTransactionResponseModel returnValue = new CustomerInventoryTransactionResponseModel();
		CustomerInventoryTransactionEntity customerInventoryTransactionEntity = customerInventoryTransactionRepository.findByCustomerInventoryTransactionIdAndIsDeleted(customerInventoryTransactionId, false);
    	BeanUtils.copyProperties(customerInventoryTransactionEntity, returnValue);
    	
    
    	UserEntity userEntity= userRepository.findByUserId(customerInventoryTransactionEntity.getCreatedBy());
		if(userEntity!=null)
			returnValue.setCustomer(userEntity.getFirstName()+" "+userEntity.getLastName());
    	else
    		returnValue.setCustomer("");
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(customerInventoryTransactionEntity.getInventoryId(), false);
		if(inventoryEntity!=null) {
			returnValue.setInventoryName(inventoryEntity.getInventoryBrandName()+" "+inventoryEntity.getInventoryGenericName());
		}
		TransactionReasonEntity transactionReasonEntity = transactionReasonRepository.findByTransactionReasonIdAndIsDeleted(customerInventoryTransactionEntity.getTransactionReasonId(), false);
    	if(transactionReasonEntity!=null) {
    		BeanUtils.copyProperties(transactionReasonEntity, returnValue);
    		returnValue.setTransactionReason(transactionReasonEntity.getTransactionReason());
    	}
		return returnValue;
	}

	@Override
	public CustomerInventoryTransactionResponseModel updateCustomerInventoryTransaction(
			long customerInventoryTransactionId,
			CustomerInventoryTransactionRequestModel customerInventoryTransactionDetail) {
		// TODO Auto-generated method stub
		   
			CustomerInventoryTransactionResponseModel returnValue = new CustomerInventoryTransactionResponseModel();
			CustomerInventoryTransactionEntity customerInventoryTransactionEntity = customerInventoryTransactionRepository.findByCustomerInventoryTransactionIdAndIsDeleted(customerInventoryTransactionId, false);
	    	if(customerInventoryTransactionEntity==null) throw new AppException("No data with this id");
			if(customerInventoryTransactionEntity.getQuantity()<customerInventoryTransactionDetail.getQuantity() && customerInventoryTransactionDetail.getTransactionType().equals("Out")) {
				 List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerInventoryTransactionDetail.getInventoryId(), false);
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
				   double difference=0;
				   if(customerInventoryTransactionEntity.getTransactionType().equals("Out")) {
				          difference=availableQuantity-outQuantity+customerInventoryTransactionEntity.getQuantity();
				   }
				   else if(customerInventoryTransactionEntity.getTransactionType().equals("In")) {
					   difference=availableQuantity-outQuantity-customerInventoryTransactionEntity.getQuantity();
				   }
				   if(difference<customerInventoryTransactionDetail.getQuantity()) {
					   return null;
				   }
			}
			BeanUtils.copyProperties(customerInventoryTransactionDetail,customerInventoryTransactionEntity);
	    	CustomerInventoryTransactionEntity savedCustomerInventoryTransactionEntity = customerInventoryTransactionRepository.save(customerInventoryTransactionEntity);
	    	BeanUtils.copyProperties(savedCustomerInventoryTransactionEntity, returnValue);
	    	UserEntity userEntity= userRepository.findByUserId(savedCustomerInventoryTransactionEntity.getCreatedBy());
			if(userEntity!=null)
				returnValue.setCustomer(userEntity.getFirstName()+" "+userEntity.getLastName());
	    	else
	    		returnValue.setCustomer("");
			InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(savedCustomerInventoryTransactionEntity.getInventoryId(), false);
			if(inventoryEntity!=null) {
				returnValue.setInventoryName(inventoryEntity.getInventoryBrandName()+" "+inventoryEntity.getInventoryGenericName());
			}
			return returnValue;
	}

	@Override
	public String deleteCustomerInventoryTransaction(long customerInventoryTransactionId) {
		// TODO Auto-generated method stub
		CustomerInventoryTransactionEntity customerInventoryTransactionEntity = customerInventoryTransactionRepository.findByCustomerInventoryTransactionIdAndIsDeleted(customerInventoryTransactionId, false);
		if(customerInventoryTransactionEntity==null) throw new AppException("no data with this id");
		customerInventoryTransactionEntity.setDeleted(true);
		customerInventoryTransactionRepository.save(customerInventoryTransactionEntity);
		
		return "Deleted!";
	}

	@Override
	public List<CustomerInventoryTransactionResponseModel> getAllCustomerFilteredInventories(int page, int limit,
			String searchKey) {
		// TODO Auto-generated method stub
//		for(CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetailResponseModel:customerSoldInventoryDetail.getCustomerSoldInventoryDetailRequestModels()) {
//			   List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerSoldInventoryDetailResponseModel.getInventoryId(), false);
//			   
//			   float availableQuantity=0;
//			   float outQuantity =0;
//			   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity:customerInventoryTransactionEntities) {   
//				   if(customerInventoryTransactionEntity.getTransactionType()=="In") {
//					  availableQuantity=availableQuantity+ customerInventoryTransactionEntity.getQuantity();
//				   }
//				   else if(customerInventoryTransactionEntity.getTransactionType()=="Out") {
//					   outQuantity = outQuantity+customerInventoryTransactionEntity.getQuantity();
//				   } 
//			   }
//			   if(customerSoldInventoryDetailResponseModel.getQuantity()>availableQuantity) {
//				   return null;
//			   }
//			}
		return null;
	}

}
