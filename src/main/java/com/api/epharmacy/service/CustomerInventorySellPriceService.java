package com.api.epharmacy.service;

import java.util.List;

import com.api.epharmacy.ui.model.request.CustomerInventorySellPriceRequestModel;
import com.api.epharmacy.ui.model.response.CustomerInventorySellPriceResponseModel;

public interface CustomerInventorySellPriceService {

	String deleteCustomerInventorySellPrice(long customerInventorySellPriceId);

	CustomerInventorySellPriceResponseModel updateCustomerInventorySellPrice(long customerInventorySellPriceId,
			CustomerInventorySellPriceRequestModel customerInventorySellPriceDetail);

	CustomerInventorySellPriceResponseModel getCustomerInventorySellPrice(long customerInventorySellPriceId);

	List<CustomerInventorySellPriceResponseModel> getAllCustomerInventorySellPrices(int page, int limit,
			long inventoryId, long companyId);

	CustomerInventorySellPriceResponseModel saveCustomerInventorySelllPrice(
			CustomerInventorySellPriceRequestModel customerInventorySellPriceDetail);

}
