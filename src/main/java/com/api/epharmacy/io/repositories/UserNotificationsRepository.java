package com.api.epharmacy.io.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.api.epharmacy.io.entity.UserNotificationsEntity;

@Repository
public interface UserNotificationsRepository extends PagingAndSortingRepository<UserNotificationsEntity, Long> {

	UserNotificationsEntity findByUserNotificationsId(long userNotificationsId);

	List<UserNotificationsEntity> findBySenderId(String senderId, Pageable pageableRequest);

	List<UserNotificationsEntity> findByReceiverId(String receiverId, Pageable pageableRequest);

	List<UserNotificationsEntity> findBySenderIdAndReceiverId(String senderId, String receiverId,
			Pageable pageableRequest);

	List<UserNotificationsEntity> findByReceiverIdOrReceiverIsRoleAndReceiverId(String receiverId, boolean b,
			String string, Pageable pageableRequest);

	List<UserNotificationsEntity> findByReceiverIdOrReceiverIsRoleAndReceiverIdAndSeen(String receiverId, boolean b,
	        String string, boolean c, Pageable pageableRequest);
	
	List<UserNotificationsEntity> findByReceiverIdAndSeen(String receiverId, boolean b, Pageable pageableRequest);
	
}
