package com.bookcorner.notification.service.serviceimpl;

import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.security.AuthenticationService;
import com.bookcorner.notification.dto.NotificationResponse;
import com.bookcorner.notification.entity.Notification;
import com.bookcorner.notification.enums.NotificationType;
import com.bookcorner.notification.exception.NotificationNotFoundException;
import com.bookcorner.notification.mapper.NotificationMapper;
import com.bookcorner.notification.repository.NotificationRepository;
import com.bookcorner.notification.service.NotificationService;
import com.bookcorner.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AuthenticationService authenticationService;

    @Override
    @Transactional
    public void send(User user, NotificationType type, String title, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    @Override
    public PageResponse<NotificationResponse> getMyNotifications(Pageable pageable) {
        User user = authenticationService.getAuthenticatedUser();
        Page<Notification> page = notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return PageResponse.<NotificationResponse>builder()
                .content(page.getContent().stream().map(notificationMapper::toNotificationResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    @Override
    public long getUnreadCount() {
        User user = authenticationService.getAuthenticatedUser();
        return notificationRepository.countByUserAndReadFalse(user);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        User user = authenticationService.getAuthenticatedUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification not found: " + notificationId));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new NotificationNotFoundException("Notification not found: " + notificationId);
        }
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        User user = authenticationService.getAuthenticatedUser();
        notificationRepository.markAllReadByUser(user);
    }
}
