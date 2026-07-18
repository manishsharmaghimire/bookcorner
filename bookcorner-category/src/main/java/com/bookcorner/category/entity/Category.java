package com.bookcorner.category.entity;


import com.bookcorner.category.enums.CategoryStatus;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Category extends BaseEntity {


    @Column(name = "category_name", nullable = false, length = 100,unique = true)
    private String categoryName;

    @Column(name = "category_description",length = 50)
    private String categoryDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryStatus status;

}
