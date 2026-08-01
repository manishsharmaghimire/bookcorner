package com.bookcorner.notification.service;

import com.bookcorner.auth.entity.User;
import com.bookcorner.notification.dto.NotificationResponse;
import com.bookcorner.notification.enums.NotificationType;
import com.bookcorner.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    void send(User user, NotificationType type, String title, String message);

    PageResponse<NotificationResponse> getMyNotifications(Pageable pageable);

    long getUnreadCount();

    void markAsRead(Long notificationId);

    void markAllAsRead();
}
