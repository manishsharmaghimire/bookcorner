package com.bookcorner.notification.mapper;

import com.bookcorner.notification.dto.NotificationResponse;
import com.bookcorner.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {


    public NotificationResponse toResponse(Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .Read(notification.IsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}