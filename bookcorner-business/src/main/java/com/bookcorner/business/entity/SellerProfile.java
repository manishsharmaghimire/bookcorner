package com.bookcorner.business.entity;

import com.bookcorner.auth.entity.User;
import com.bookcorner.business.enums.SellerStatus;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "seller_profiles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seller_profiles_user",
                        columnNames = "user_id"
                )
        },
        indexes = {
                @Index(
                        name = "idx_seller_profiles_status",
                        columnList = "status"
                )
        }
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SellerProfile  extends BaseEntity {

    @Column(
            name = "display_name",
            nullable = false,
            length = 100
    )
    private String displayName;

    @Column(
            name = "bio",
            length = 1000
    )
    private String bio;

    @Column(
            name = "profile_image",
            length = 500
    )
    private String profileImage;

    @Column(
            name = "rating",
            nullable = false,
            precision = 3,
            scale = 2
    )
    private BigDecimal rating;

    @Column(
            name = "review_count",
            nullable = false
    )
    private Long reviewCount;

    @Column(
            name = "sales_count",
            nullable = false
    )
    private Long salesCount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private SellerStatus status = SellerStatus.ACTIVE;;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_seller_profiles_user")
    )
    private User user;
}


