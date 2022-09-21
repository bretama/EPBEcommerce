package com.api.epharmacy.service.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.epharmacy.exception.AppException;
import com.api.epharmacy.io.entity.TransactionReasonEntity;
import com.api.epharmacy.io.repositories.TransactionReasonRepository;
import com.api.epharmacy.service.TransactionReasonService;
import com.api.epharmacy.ui.model.request.TransactionReasonRequestModel;
import com.api.epharmacy.ui.model.response.TransactionReasonResponseModel;


@Service
@Transactional
public class TransactionReasonServiceImpl implements TransactionReasonService {

	@Autowired
	TransactionReasonRepository transactionReasonRepository;
	@Override
	public String deleteTransactionReason(Integer transactionReasonId) {
		// TODO Auto-generated method stub
		TransactionReasonEntity transactionReasonEntity = transactionReasonRepository.findByTransactionReasonIdAndIsDeleted(transactionReasonId,false);
		if(transactionReasonEntity==null) throw new AppException("no transaction reason with this id");
		transactionReasonEntity.setDeleted(true);
		transactionReasonRepository.save(transactionReasonEntity);
		return "Deleted Successfully!";
	}

	@Override
	public TransactionReasonResponseModel updateTransactionReason(TransactionReasonRequestModel transactionReasonDetail,
			Integer transactionReasonId) {
		// TODO Auto-generated method stub
		TransactionReasonResponseModel returnValue = new TransactionReasonResponseModel();
		TransactionReasonEntity transactionReasonEntity = transactionReasonRepository.findByTransactionReasonIdAndIsDeleted(transactionReasonId, false);
		if(transactionReasonEntity==null) throw new AppException("no data with this id");
		BeanUtils.copyProperties(transactionReasonDetail, transactionReasonEntity);
		TransactionReasonEntity savedTransactionReasonEntity = transactionReasonRepository.save(transactionReasonEntity);
		BeanUtils.copyProperties(savedTransactionReasonEntity, returnValue);
		return returnValue;
	}

	@Override
	public TransactionReasonResponseModel getTransactionReason(Integer transactionReasonId) {
		// TODO Auto-generated method stub
		TransactionReasonResponseModel returnValue = new TransactionReasonResponseModel();
		TransactionReasonEntity transactionReasonEntity = transactionReasonRepository.findByTransactionReasonIdAndIsDeleted(transactionReasonId, false);
		if(transactionReasonEntity==null) throw new AppException("no data with htis id");
		BeanUtils.copyProperties(transactionReasonEntity, returnValue);
		return returnValue;
	}

	@Override
	public List<TransactionReasonResponseModel> getAllTransactionReasons(int page, int limit, String searchKey) {
		// TODO Auto-generated method stub
		List<TransactionReasonResponseModel> returnValue = new ArrayList<>();
		if(page>0)
			page = page-1;
		Pageable pageableRequest = PageRequest.of(page, limit, Sort.by("transactionReasonId").descending());
		Page<TransactionReasonEntity> transactionReasonPage =null;
		if("".equals(searchKey))
			transactionReasonPage= transactionReasonRepository.findByIsDeleted(false,pageableRequest);
		else {
			transactionReasonPage = transactionReasonRepository.findByTransactionReasonContainingAndIsDeleted(searchKey,false,pageableRequest);
		}
		List<TransactionReasonEntity> transactionReasonEntities = transactionReasonPage.getContent();
		int totalPages = transactionReasonPage.getTotalPages();
		for(TransactionReasonEntity transactionReasonEntity:transactionReasonEntities) {
			TransactionReasonResponseModel transactionReasonResponseModel = new TransactionReasonResponseModel();
			BeanUtils.copyProperties(transactionReasonEntity, transactionReasonResponseModel);
			if(returnValue.size()==0) {
				transactionReasonResponseModel.setTotalPages(totalPages);
			}
			returnValue.add(transactionReasonResponseModel);
		}
		
		return returnValue;
	}

	@Override
	public TransactionReasonResponseModel saveTransctionReason(TransactionReasonRequestModel transactionReasonDetail) {
		// TODO Auto-generated method stub
		TransactionReasonResponseModel returnValue = new TransactionReasonResponseModel();
		TransactionReasonEntity transactionReasonEntity = new TransactionReasonEntity();
		BeanUtils.copyProperties(transactionReasonDetail, transactionReasonEntity);
		TransactionReasonEntity savedTransactionReasonEntity = transactionReasonRepository.save(transactionReasonEntity);
		BeanUtils.copyProperties(savedTransactionReasonEntity, returnValue);
		return returnValue;
	}

}
