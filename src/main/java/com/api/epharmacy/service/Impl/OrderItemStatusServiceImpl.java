package com.api.epharmacy.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.jboss.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.epharmacy.exception.AppException;
import com.api.epharmacy.io.entity.CustomerInventoryTransactionEntity;
import com.api.epharmacy.io.entity.CustomerPurchasedInventoryEntity;
import com.api.epharmacy.io.entity.InventorySellPriceListEntity;
import com.api.epharmacy.io.entity.OrderEntity;
import com.api.epharmacy.io.entity.OrderItemEntity;
import com.api.epharmacy.io.entity.OrderItemStatusEntity;
import com.api.epharmacy.io.entity.OrderStatusTypeEntity;
import com.api.epharmacy.io.entity.OrderTypeOrderStatusTypeEntity;
import com.api.epharmacy.io.repositories.CompanyRepository;
import com.api.epharmacy.io.repositories.CustomerInventoryTransactionRepository;
import com.api.epharmacy.io.repositories.CustomerPurchasedInventoryRepository;
import com.api.epharmacy.io.repositories.InventoryCostPriceListRepository;
import com.api.epharmacy.io.repositories.InventoryRepository;
import com.api.epharmacy.io.repositories.InventorySellPriceListRepository;
import com.api.epharmacy.io.repositories.InventoryTransactionDetailRepository;
import com.api.epharmacy.io.repositories.OrderDocumentsRepository;
import com.api.epharmacy.io.repositories.OrderItemRepository;
import com.api.epharmacy.io.repositories.OrderItemStatusRepository;
import com.api.epharmacy.io.repositories.OrderRepository;
import com.api.epharmacy.io.repositories.OrderStatusTypeRepository;
import com.api.epharmacy.io.repositories.OrderTypeOrderStatusTypeRepository;
import com.api.epharmacy.io.repositories.OrderTypeRepository;
import com.api.epharmacy.io.repositories.StockSiteRepository;
import com.api.epharmacy.io.repositories.UserRepository;
import com.api.epharmacy.model.audit.Audit;
import com.api.epharmacy.security.JwtTokenProvider;
import com.api.epharmacy.service.OrderItemStatusService;
import com.api.epharmacy.service.UserNotificationsService;
import com.api.epharmacy.shared.GenerateRandomString;
import com.api.epharmacy.ui.model.request.OrderItemStatusRequestModel;
import com.api.epharmacy.ui.model.request.OrderItemsStatusRequestModel;
import com.api.epharmacy.ui.model.request.OrderStatusInfoRequestModel;
import com.api.epharmacy.ui.model.request.SingleOrderItemStatusRequestModel;
import com.api.epharmacy.ui.model.response.OrderItemStatusResponseModel;
import com.api.epharmacy.ui.model.response.OrderItemsStatusResponseModel;
import com.api.epharmacy.ui.model.response.OrderStatusInfoResponseModel;

@Service
public class OrderItemStatusServiceImpl implements OrderItemStatusService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	InventoryCostPriceListRepository inventoryCostPriceListRepository;
	
	@Autowired
	InventoryTransactionDetailRepository inventoryTransactionDetailRepository;
	
	@Autowired
	GenerateRandomString generateRandomString;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	StockSiteRepository stockSiteRepository;

	@Autowired
	OrderDocumentsRepository orderDocumentsRepository;
	
	@Autowired
	UserNotificationsService userNotificationsService;

    @Autowired
    private JwtTokenProvider tokenProvider;

	@Autowired
	EntityManager entityManager;
	
	@Autowired
	OrderStatusTypeRepository orderStatusTypeRepository;
	
	@Autowired
	OrderTypeRepository orderTypeRepository;
	
	@Autowired
	OrderItemStatusRepository orderItemStatusRepository;

	@Autowired
	OrderTypeOrderStatusTypeRepository orderTypeOrderStatusTypeRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	CustomerPurchasedInventoryRepository customerPurchasedInventoryRepository;
	
	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;
	
	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	private String rootDirectory = "src";

	private String uploadDir = rootDirectory + "/uploadedOrderDocuments/";

	Logger LOGGER = Logger.getLogger(OrderItemStatusServiceImpl.class.getName());

	@Override
	public OrderStatusInfoResponseModel getOrderItemsStatusByOrderId(long orderId) {
		OrderStatusInfoResponseModel returnValue=new OrderStatusInfoResponseModel();
		List<OrderItemsStatusResponseModel> orderItemsStatusInfo=new ArrayList<>();
		List<OrderItemEntity> orderItemEntities=orderItemRepository.findByOrderIdAndIsDeleted(orderId, false);
		for(OrderItemEntity orderItemEntity: orderItemEntities) {
			OrderItemsStatusResponseModel orderItemsStatusResponseModel=new OrderItemsStatusResponseModel();
			BeanUtils.copyProperties(orderItemEntity, orderItemsStatusResponseModel);
			List<OrderItemStatusResponseModel> orderItemStatuses=new ArrayList<>();
			List<OrderItemStatusEntity> saveOrderItemStatusEntities=orderItemStatusRepository.findByOrderItemIdAndIsDeleted(orderItemEntity.getOrderItemId(), false);

			OrderEntity orderEntity=orderRepository.findByOrderIdAndIsDeleted(orderId, false);
			OrderTypeOrderStatusTypeEntity orderTypeOrderStatusTypeEntity=orderTypeOrderStatusTypeRepository.findTopByOrderTypeIdAndIsDeletedOrderByWeight(orderEntity.getOrderTypeId(), false);
			for(OrderItemStatusEntity orderItemStatusEntity: saveOrderItemStatusEntities) {
				OrderItemStatusResponseModel orderItemStatusResponseModel=new OrderItemStatusResponseModel();
				BeanUtils.copyProperties(orderItemStatusEntity, orderItemStatusResponseModel);
				
    			if(orderTypeOrderStatusTypeEntity!=null && orderItemStatusEntity.getOrderStatusTypeId() == orderTypeOrderStatusTypeEntity.getOrderStatusTypeId()) 
    				orderItemStatusResponseModel.setQuantity(orderItemStatusResponseModel.getQuantity() - orderItemEntity.getPreOrderQuantity());
				orderItemStatuses.add(orderItemStatusResponseModel);
			}
			orderItemsStatusResponseModel.setStatuses(orderItemStatuses);
			orderItemsStatusInfo.add(orderItemsStatusResponseModel);
		}
		returnValue.setOrderItemsStatusInfo(orderItemsStatusInfo);
		return returnValue;
	}

	@Override
	public String changeOrderItemsStatus(OrderStatusInfoRequestModel orderStatusInfoRequestModel) {
		for(OrderItemsStatusRequestModel orderItemsStatusDetail: orderStatusInfoRequestModel.getOrderItemsStatusInfo()) {
			
			List<OrderItemStatusEntity> orderItemStatusEntities=orderItemStatusRepository.findByOrderItemIdAndIsDeleted(orderItemsStatusDetail.getOrderItemId(), false);
			orderItemStatusRepository.deleteAll(orderItemStatusEntities);

			for(OrderItemStatusRequestModel orderItemStatus: orderItemsStatusDetail.getStatuses()) {
				// filter empty submitions
				if(orderItemStatus.getOrderStatusTypeId()==null)
					continue;
				
				OrderItemStatusEntity orderItemStatusEntity=orderItemStatusRepository.findTopByOrderItemIdAndOrderStatusTypeIdAndIsDeleted(orderItemsStatusDetail.getOrderItemId(), orderItemStatus.getOrderStatusTypeId(), false);
				if(orderItemStatusEntity==null)
					orderItemStatusEntity=new OrderItemStatusEntity();
				BeanUtils.copyProperties(orderItemStatus, orderItemStatusEntity);
				orderItemStatusEntity.setOrderItemId(orderItemsStatusDetail.getOrderItemId());
				
				OrderItemEntity orderItemEntity=orderItemRepository.findByOrderItemIdAndIsDeleted(orderItemsStatusDetail.getOrderItemId(), false);
				OrderEntity orderEntity=orderRepository.findByOrderIdAndIsDeleted(orderItemEntity.getOrderId(), false);
				OrderTypeOrderStatusTypeEntity orderTypeOrderStatusTypeEntity=orderTypeOrderStatusTypeRepository.findTopByOrderTypeIdAndIsDeletedOrderByWeight(orderEntity.getOrderTypeId(), false);
    			if(orderTypeOrderStatusTypeEntity!=null && orderItemStatusEntity.getOrderStatusTypeId() == orderTypeOrderStatusTypeEntity.getOrderStatusTypeId()) 
    				orderItemStatusEntity.setQuantity(orderItemStatusEntity.getQuantity() + orderItemEntity.getPreOrderQuantity());

				orderItemStatusRepository.save(orderItemStatusEntity);
				
				OrderStatusTypeEntity orderStatusTypeEntity = orderStatusTypeRepository.findByOrderStatusTypeIdAndOrderStatusTypeAndIsDeleted(orderItemStatusEntity.getOrderStatusTypeId(), "Picked Up", false);
				if(orderStatusTypeEntity != null) {
					CustomerPurchasedInventoryEntity customerPurchasedInventoryEntity = new CustomerPurchasedInventoryEntity();
					InventorySellPriceListEntity inventorySellPriceListEntity = inventorySellPriceListRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(orderItemStatusEntity.getOrderItemId(), Instant.now(), false);
					if(inventorySellPriceListEntity==null) 
						throw new AppException("Sell Price not found "+orderItemStatusEntity.getOrderItemId()+" @ "+Instant.now());
					customerPurchasedInventoryEntity.setCustomerId(orderEntity.getCreatedBy());					
					customerPurchasedInventoryEntity.setInventoryId(orderItemStatusEntity.getOrderItemId());
					customerPurchasedInventoryEntity.setQuantity(orderItemStatusEntity.getQuantity());
					customerPurchasedInventoryEntity.setCostPrice(inventorySellPriceListEntity.getSellPrice() - inventorySellPriceListEntity.getDiscountAmount());
					customerPurchasedInventoryEntity.setPurchasedDate(orderItemStatusEntity.getStatusDateTime());
					CustomerPurchasedInventoryEntity savedCustomerPurchasedInventoryEntity=customerPurchasedInventoryRepository.save(customerPurchasedInventoryEntity);	
					
					CustomerInventoryTransactionEntity customerInventoryTransactionEntity=new CustomerInventoryTransactionEntity();
					customerInventoryTransactionEntity.setCustomerId(savedCustomerPurchasedInventoryEntity.getCustomerId());
					customerInventoryTransactionEntity.setInventoryId(savedCustomerPurchasedInventoryEntity.getInventoryId());
					customerInventoryTransactionEntity.setQuantity(savedCustomerPurchasedInventoryEntity.getQuantity());
					customerInventoryTransactionEntity.setTransactionType("In");
					customerInventoryTransactionEntity.setTransactionDate(savedCustomerPurchasedInventoryEntity.getPurchasedDate());
					customerInventoryTransactionRepository.save(customerInventoryTransactionEntity);
				}
			}
		}
		return "Status updated successfully!";
	}

	@Override
	public String changeOrderItemsReadyForPickupDate(SingleOrderItemStatusRequestModel orderItemsStatusDetail) {
				OrderItemStatusEntity orderItemStatusEntity=orderItemStatusRepository.findTopByOrderItemIdAndOrderStatusTypeIdAndIsDeleted(orderItemsStatusDetail.getOrderItemId(), orderItemsStatusDetail.getOrderStatusTypeId(), false);
				if(orderItemStatusEntity==null)
					throw new AppException("No order item found with id: "+orderItemsStatusDetail.getOrderItemId()+" and status "+orderItemsStatusDetail.getOrderStatusTypeId());

				BeanUtils.copyProperties(orderItemsStatusDetail, orderItemStatusEntity);
				orderItemStatusRepository.save(orderItemStatusEntity);
				
		return "Status updated successfully!";
	}
}
