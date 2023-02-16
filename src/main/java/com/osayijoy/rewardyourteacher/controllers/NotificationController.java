package com.decagon.rewardyourteacher.controllers;


import com.decagon.rewardyourteacher.dto.APIResponse;
import com.decagon.rewardyourteacher.dto.NotificationResponseDTO;
import com.decagon.rewardyourteacher.entity.Notification;
import com.decagon.rewardyourteacher.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("user-notifications")
    public ResponseEntity<APIResponse<List<NotificationResponseDTO>>> getUserNotifications (final Principal principal) {
        final String email = principal.getName();
        try {
            final List<NotificationResponseDTO> notifications = notificationService.getAllNotificationsOfUser(email);
            return ResponseEntity.ok(new APIResponse<>(true, "success", notifications));
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
