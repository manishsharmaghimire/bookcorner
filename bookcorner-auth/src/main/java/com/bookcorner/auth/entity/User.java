package com.bookcorner.auth.entity;


import com.bookcorner.auth.enums.Role;
import com.bookcorner.auth.enums.UserStatus;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_phone_number",
                        columnNames = "phone_number"
                )
        }
)
public class User extends BaseEntity {

    @Column(
            name = "phone_number",
            nullable = false,
            length = 15
    )
    private String phoneNumber;

    @Column(
            name = "password_hash",
            nullable = false
    )
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false,
            length = 20
    )
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private UserStatus status;

}