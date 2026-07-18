package com.bookcorner.publisher.entity;


import com.bookcorner.publisher.enums.PublisherStatus;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "publishers", uniqueConstraints = {

        @UniqueConstraint(
                name = "uk_publisher_name",
                columnNames = {"publisherName"})

})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Publisher extends BaseEntity {


    @Column(name = "publisher_name", nullable = false, length = 100, unique = true)
    private String publisherName;

    @Column(name = "publisher_address", length = 500)
    private String publisherAddress;

    @Column(name = "publisher_contact", length = 20)
    private String publisherContact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublisherStatus status;

}
