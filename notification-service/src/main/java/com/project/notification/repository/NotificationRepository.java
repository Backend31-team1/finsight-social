package com.project.notification.repository;

import com.project.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 알림 엔티티 저장소입니다.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}