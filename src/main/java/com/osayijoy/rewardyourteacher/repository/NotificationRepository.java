package com.decagon.rewardyourteacher.repository;
import com.decagon.rewardyourteacher.entity.Notification;
import com.decagon.rewardyourteacher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select * from Notification where user.Id = ?1", nativeQuery = true)
    List<Notification> findNotificationsByUserId(Long user_id);
    List<Notification> findNotificationsByUserOrderByCreatedAtDesc(User user);
}
