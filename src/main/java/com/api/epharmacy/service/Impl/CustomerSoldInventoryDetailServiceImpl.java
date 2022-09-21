package com.api.epharmacy.service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.api.epharmacy.exception.AppException;
import com.api.epharmacy.io.entity.CustomerInventoryTransactionEntity;
import com.api.epharmacy.io.entity.CustomerSoldInventoryDetailEntity;
import com.api.epharmacy.io.entity.UserEntity;
import com.api.epharmacy.io.repositories.CustomerInventorySellPriceRepository;
import com.api.epharmacy.io.repositories.CustomerInventoryTransactionRepository;
import com.api.epharmacy.io.repositories.CustomerSoldInventoryDetailRepository;
import com.api.epharmacy.io.repositories.UserRepository;
import com.api.epharmacy.service.CustomerSoldInventoryDetailService;
import com.api.epharmacy.ui.model.request.CustomerSoldInventoryDetailRequestModel;
import com.api.epharmacy.ui.model.response.CustomerSoldInventoryDetailResponseModel;

@Service
@Transactional
public class CustomerSoldInventoryDetailServiceImpl implements CustomerSoldInventoryDetailService{

	@Autowired
	CustomerSoldInventoryDetailRepository customerSoldInventoryDetailRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	@Autowired
	CustomerInventorySellPriceRepository customerInventorySellPriceRepository;
	@Override
	public CustomerSoldInventoryDetailResponseModel updateCustomerSoldInventoryDetail(
			CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetail, long customerSoldInventoryDetailId) {
		// TODO Auto-generated method stub
		CustomerSoldInventoryDetailResponseModel returnValue = new CustomerSoldInventoryDetailResponseModel();
		CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity = customerSoldInventoryDetailRepository.findByCustomerSoldInventoryDetailIdAndIsDeleted(customerSoldInventoryDetailId, false);
//		if(customerSoldInventoryDetailEntity.getQuantity()==customerSoldInventoryDetail.getQuantity()) {
//			
//		}
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserEntity userEntity = userRepository.findByUserId(auth.getName());
		if(customerSoldInventoryDetailEntity.getQuantity()>customerSoldInventoryDetail.getQuantity()) {
			CustomerInventoryTransactionEntity customerInventoryTransactionEntity = new CustomerInventoryTransactionEntity();
			customerInventoryTransactionEntity.setTransactionType("In");
			customerInventoryTransactionEntity.setQuantity(customerInventoryTransactionEntity.getQuantity()-customerSoldInventoryDetail.getQuantity());

		    if(userEntity!=null) {
		    	customerInventoryTransactionEntity.setCompanyId(userEntity.getCompanyId());
		    	UserEntity userEntity2 = userRepository.findTopByCompanyIdAndUserTypeAndIsDeleted(userEntity.getCompanyId(), "Customer", false);
		    	customerInventoryTransactionEntity.setCustomerId(userEntity2.getUserId());
		    }
		    customerInventoryTransactionEntity.setCreatedBy(auth.getName());
//			customerInventoryTransactionEntity.setCustomerId(customerSoldInventoryDetail.getCustomerId());
			customerInventoryTransactionEntity.setInventoryId(customerSoldInventoryDetail.getInventoryId());
 			CustomerInventoryTransactionEntity savedCustomerInventoryTransactionEntity = customerInventoryTransactionRepository.save(customerInventoryTransactionEntity);
			BeanUtils.copyProperties(savedCustomerInventoryTransactionEntity, returnValue);
		}
		else if(customerSoldInventoryDetailEntity.getQuantity()<customerSoldInventoryDetail.getQuantity()){
			double availableQuantity=0;
			 double outQuantity =0;
			   List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerSoldInventoryDetail.getInventoryId(), false);
			   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity:customerInventoryTransactionEntities) {   
				   if(customerInventoryTransactionEntity.getTransactionType()=="In") {
					  availableQuantity=availableQuantity+ customerInventoryTransactionEntity.getQuantity();
				   }
				   else if(customerInventoryTransactionEntity.getTransactionType()=="Out") {
					   outQuantity = outQuantity+customerInventoryTransactionEntity.getQuantity();
				   }           
			   }
			   double difference=availableQuantity-outQuantity+customerSoldInventoryDetailEntity.getQuantity();
			   if(difference<customerSoldInventoryDetail.getQuantity()) {
				   return null;
			   }
			   else {
			   
					CustomerInventoryTransactionEntity customerInventoryTransactionEntity = new CustomerInventoryTransactionEntity();
					customerInventoryTransactionEntity.setTransactionType("Out");
					customerInventoryTransactionEntity.setQuantity(customerSoldInventoryDetail.getQuantity()-customerInventoryTransactionEntity.getQuantity());
//					customerInventoryTransactionEntity.setCustomerId(customerSoldInventoryDetail.getCustomerId());
				    if(userEntity!=null) {
				    	customerInventoryTransactionEntity.setCompanyId(userEntity.getCompanyId());
				    	UserEntity userEntity2 = userRepository.findTopByCompanyIdAndUserTypeAndIsDeleted(userEntity.getCompanyId(), "Customer", false);
				    	customerInventoryTransactionEntity.setCustomerId(userEntity2.getUserId());
				    }
				    customerInventoryTransactionEntity.setCreatedBy(auth.getName());
					customerInventoryTransactionEntity.setInventoryId(customerSoldInventoryDetail.getInventoryId());
				
					CustomerInventoryTransactionEntity savedCustomerInventoryTransactionEntity = customerInventoryTransactionRepository.save(customerInventoryTransactionEntity);
					BeanUtils.copyProperties(savedCustomerInventoryTransactionEntity, returnValue);
			   }
			
		}
		BeanUtils.copyProperties(customerSoldInventoryDetail, customerSoldInventoryDetailEntity);
	
		customerSoldInventoryDetailEntity.setCreatedBy(auth.getName());
		CustomerSoldInventoryDetailEntity savedCustomerSoldInventoryDetailEntity = customerSoldInventoryDetailRepository.save(customerSoldInventoryDetailEntity);
		BeanUtils.copyProperties(savedCustomerSoldInventoryDetailEntity, returnValue);
		return returnValue;
	}
	@Override
	public CustomerSoldInventoryDetailResponseModel getCustomerSoldInventoryDetail(long customerSoldInventoryDetailId) {
		// TODO Auto-generated method stub
		CustomerSoldInventoryDetailResponseModel returnValue = new CustomerSoldInventoryDetailResponseModel();
		CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity = customerSoldInventoryDetailRepository.findByCustomerSoldInventoryDetailIdAndIsDeleted(customerSoldInventoryDetailId, false);
		if(customerSoldInventoryDetailEntity==null) throw new AppException("no data with this id");
		BeanUtils.copyProperties(customerSoldInventoryDetailEntity, returnValue);
		return returnValue;
	}
	@Override
	public String deleteCustomerSoldInventoryDetail(long customerSoldInventoryDetailId) {
		// TODO Auto-generated method stub
		CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity = customerSoldInventoryDetailRepository.findByCustomerSoldInventoryDetailIdAndIsDeleted(customerSoldInventoryDetailId, false);
		if(customerSoldInventoryDetailEntity==null) throw new AppException("no data with this id");
		customerSoldInventoryDetailEntity.setDeleted(true);
		customerSoldInventoryDetailRepository.save(customerSoldInventoryDetailEntity);
		return "Deleted Successfully!";
	}

}
