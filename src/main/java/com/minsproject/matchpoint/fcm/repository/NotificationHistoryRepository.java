package com.minsproject.matchpoint.fcm.repository;

import com.minsproject.matchpoint.fcm.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long>, NotificationHistoryCustomRepository {
}