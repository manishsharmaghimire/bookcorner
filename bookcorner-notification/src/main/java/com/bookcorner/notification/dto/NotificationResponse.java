package com.bookcorner.notification.dto;

import com.bookcorner.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class NotificationResponse {



    private long id;
    private String title;
    private String message;
    private NotificationType status;
    private boolean isRead;
    private Instant createdAt;
}
