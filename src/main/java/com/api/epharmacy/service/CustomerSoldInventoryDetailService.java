package com.api.epharmacy.service;

import com.api.epharmacy.ui.model.request.CustomerSoldInventoryDetailRequestModel;
import com.api.epharmacy.ui.model.response.CustomerSoldInventoryDetailResponseModel;

public interface CustomerSoldInventoryDetailService {

	CustomerSoldInventoryDetailResponseModel updateCustomerSoldInventoryDetail(
			CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetail, long customerSoldInventoryDetailId);

	CustomerSoldInventoryDetailResponseModel getCustomerSoldInventoryDetail(long customerSoldInventoryDetailId);

	String deleteCustomerSoldInventoryDetail(long customerSoldInventoryDetailId);

}
