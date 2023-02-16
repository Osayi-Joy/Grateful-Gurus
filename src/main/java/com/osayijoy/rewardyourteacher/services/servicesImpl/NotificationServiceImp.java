package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.dto.MessageDTO;
import com.decagon.rewardyourteacher.dto.NotificationResponseDTO;
import com.decagon.rewardyourteacher.entity.*;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.enums.TransactionType;
import com.decagon.rewardyourteacher.enums.UserRole;
import com.decagon.rewardyourteacher.exceptions.CustomException;
import com.decagon.rewardyourteacher.repository.NotificationRepository;
import com.decagon.rewardyourteacher.repository.UserRepository;
import com.decagon.rewardyourteacher.services.EmailService;
import com.decagon.rewardyourteacher.services.NotificationService;
import com.decagon.rewardyourteacher.utils.EmailDetails;
import com.decagon.rewardyourteacher.utils.EpochTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.decagon.rewardyourteacher.entity.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Getter
@Setter
@Service
public class NotificationServiceImp implements NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Override
    public Notification studentSendMoneyNotification(Transaction transaction, String sender) {
        Long userId = transaction.getUser().getId();
        User user = userRepository.findById(userId).orElse(null);

        Notification notification = new Notification();
        try {
            if (user == null || user.getRole().equals(UserRole.STUDENT)) {
                throw new CustomException("User with id " + userId + " is not valid");
            }

            String message = "You have sent money #" + transaction.getAmount() + " to " + user.getFirstName();
            String title = "Transaction Alert!!! " + TransactionType.DEBIT;

            notification.setCreatedAt(LocalDateTime.now());
            notification.setMessage(message);
            notification.setUser(user);
            notification.setTitle(title);

            String email = user.getEmail();
            String msgBody = "Dear " + user.getLastName() + " "+ user.getFirstName() + ",\n" + " We wish to inform you that a "
                    + TransactionType.DEBIT + " transaction occurred on your wallet with us.\n"
                    + " The details of this transaction are shown below.";
            String mailSubject = "Transaction Alert!!! " + TransactionType.DEBIT;

            emailService.sendSimpleMail(new EmailDetails(email, msgBody, mailSubject), sender);

            notificationRepository.save(notification);


            return notification;
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
        String failureMessage = "Transaction failed!!!. Invalid user.";
        notification.setMessage(failureMessage);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }

    @Override
    public Notification walletFundingNotification(Transaction transaction, String sender){
        Long userId = transaction.getUser().getId();
        User user = userRepository.findById(userId).orElse(null);

        if (user == null || user.getRole().equals(UserRole.TEACHER)) {
            throw new CustomException("User with Id " + userId + " is not valid");
        }

        Notification notification = new Notification();
        String message = "You funded your wallet with " + "#" + transaction.getAmount();
        notification.setCreatedAt(transaction.getLocalDateTime());
        notification.setMessage(message);
        notification.setUser(user);


        String email = user.getEmail();
        String msgBody = "Dear " + user.getLastName() + " "+ user.getFirstName() + ",\n" + " We wish to inform you that you successfully "
                + " funded your wallet with " + transaction.getAmount()
                + " The details of this transaction are shown below.";
        String mailSubject = "Transaction Alert!!! - Wallet Funding. " + TransactionType.CREDIT;


        emailService.sendSimpleMail(new EmailDetails(email, msgBody, mailSubject), sender);

        notificationRepository.save(notification);


        return notification;
    }

    @Override
    public Notification studentAppreciatedNotification(String email, Long studentId, MessageDTO messageDTO) {
        User student = userRepository.findById(studentId).orElse(null);
        User teacher = userRepository.findByEmail(email);
        if (student == null || student.getRole().equals(UserRole.TEACHER)) {
            throw new CustomException("User does not exist");
        }

        Notification notification = notification("Sent successfully", teacher);
        Notification notificationToStudent = notification(teacher.getFirstName() + " " + teacher.getLastName() + "\n" +
                messageDTO.getMessage(), student);

        String studentEmail = student.getEmail();
        String msgBody = teacher.getLastName() + " " + teacher.getFirstName()  + " appreciates your kind gesture. ";
        String mailSubject = "Appreciation Mail!!!. ";

        emailService.sendSimpleMail(new EmailDetails(studentEmail, msgBody, mailSubject), email);

        notificationRepository.save(notificationToStudent);
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public Notification TeacherYouReceived(Transaction transaction, String sender) {
        Notification notification = new Notification();
        Long userId = transaction.getUser().getId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole().equals(UserRole.STUDENT)) {
            throw new CustomException("User with Id " + userId + " is not valid");
        }
        String message = "You received " + transaction.getAmount();
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(message);
        notification.setUser(user);

        String email = user.getEmail();
        String msgBody = "Dear " + user.getLastName() + " "+ user.getFirstName() + ",\n" + " We wish to inform you that you "
                + "received sum of # " + transaction.getAmount() + "in your wallet with us.\n"
                + " from a " + user.getRole().equals(UserRole.STUDENT)
                + " The details of this transaction are shown below.";
        String mailSubject = "Transaction Alert!!! " + TransactionType.CREDIT;

        emailService.sendSimpleMail(new EmailDetails(email, msgBody, mailSubject), sender);

        notificationRepository.save(notification);

        return notification;
    }
    @Override
    public Notification TeacherSentYou(Transaction transaction, String sender) {
        Notification notification = new Notification();
        Long userId = transaction.getUser().getId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole().equals(UserRole.TEACHER)) {
            throw new CustomException("User with Id " + userId + " is not valid");
        }
        String message = user.getFirstName() + " sent you N" + transaction.getAmount();

        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(message);
        notification.setUser(user);

        String email = user.getEmail();
        String msgBody = "Dear " + user.getLastName() + " "+ user.getFirstName() + ",\n" + " We wish to inform you that you "
                + "received sum of # " + transaction.getAmount() + "in your wallet with us.\n"
                + " from a " + user.getRole().equals(UserRole.TEACHER)
                + " The details of this transaction are shown below.";
        String mailSubject = "Transaction Alert!!! " + TransactionType.CREDIT;

        emailService.sendSimpleMail(new EmailDetails(email, msgBody, mailSubject), sender);
        notificationRepository.save(notification);

        return notification;
    }
    @Override
    public List<Notification> showAllNotificationsByUserId(Long userId) {
        return notificationRepository.findNotificationsByUserId(userId);
    }

    @Override
    public Notification saveNotification(User user) {
        return null;
    }

    @Override
    public Notification saveNotification(Notification notification) {

        // todo : call emailservice

        return notificationRepository.save(notification);
    }

    @Override
    public Notification saveNotification(User user, String description, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(message);
        notification.setNotificationBody(description);

        String email = user.getEmail();
        String msgBody = "Dear " + user.getLastName() + " "+ user.getFirstName() + ",\n" + " Your wallet has been successfully funded";
        String mailSubject = "Transaction Alert!!! - Wallet Funding. " + TransactionType.CREDIT;

        emailService.sendSimpleMail(new EmailDetails(email, msgBody, mailSubject), email);

        return saveNotification(notification);
    }

    @Override
    public List<NotificationResponseDTO> getAllNotificationsOfUser(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                ()->new CustomException("No user with the email "+ email));

        List<Notification> notifications = notificationRepository.findNotificationsByUserOrderByCreatedAtDesc(user);
        List<NotificationResponseDTO> notificationResponseDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationResponseDTO notificationResponseDTO = new ModelMapper()
                    .map(notification, NotificationResponseDTO.class);
            notificationResponseDTO.setUserId(user.getId());
            notificationResponseDTO.setElapsedTime(EpochTime.getElapsedTime(notification.getCreatedAt()));
            notificationResponseDTOS.add(notificationResponseDTO);
        }
        return notificationResponseDTOS;
    }

    private Notification notification(String message, User user) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }
}

