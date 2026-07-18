package com.bookcorner.shared.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;



@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)


public class BaseEntity {


        @Id
        @GeneratedValue
        @Column(nullable = false, updatable = false)
        private UUID id;

        @CreatedDate
        @Column(nullable = false, updatable = false)
        private Instant createdAt;

        @LastModifiedDate
        @Column(nullable = false)
        private Instant updatedAt;
    }

