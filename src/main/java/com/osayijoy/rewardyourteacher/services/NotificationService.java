package com.osayijoy.rewardyourteacher.services;


import com.osayijoy.rewardyourteacher.dto.MessageDTO;
import com.osayijoy.rewardyourteacher.dto.NotificationResponseDTO;
import com.osayijoy.rewardyourteacher.entity.Notification;
import com.osayijoy.rewardyourteacher.entity.Transaction;
import com.osayijoy.rewardyourteacher.entity.User;

import java.util.List;

public interface NotificationService {
    Notification studentSendMoneyNotification(Transaction transaction, String email);

    Notification walletFundingNotification(Transaction transaction, String email);

    Notification studentAppreciatedNotification(String email, Long user_id, MessageDTO messageDTO);


    Notification TeacherYouReceived(Transaction transaction, String email);

    Notification TeacherSentYou(Transaction transaction, String email);

    List<Notification> showAllNotificationsByUserId(Long notificationId);

    Notification saveNotification(User user);

    Notification saveNotification(Notification notification);

    Notification saveNotification(User user, String description, String message);

    List<NotificationResponseDTO> getAllNotificationsOfUser(String email);

}
