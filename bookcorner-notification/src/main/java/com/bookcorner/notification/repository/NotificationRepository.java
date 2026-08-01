package com.bookcorner.notification.repository;

import com.bookcorner.auth.entity.User;
import com.bookcorner.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Query("""
            SELECT n
            FROM Notification n
            WHERE n.user = :user
            ORDER BY n.createdAt DESC
            """)
    List<Notification> findNotificationByUser(@Param("user")User user);



    @Query("""

        SELECT n
        FROM Notification n
        WHERE n.user = :user
        AND n.read = false
        ORDER BY n.createdAt DESC

""")
    List<Notification> findUnreadNotificationsByUser(
            @Param("user") User user
    );

    @Query("""

        SELECT count(n)
        FROM Notification n
        WHERE n.user = :user
        AND n.read = false
        

""")

    long countUnreadNotifications(
            @Param("user") User user
    );

}
