package com.bookcorner.author.entity;


import com.bookcorner.author.enums.AuthorStatus;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authors", uniqueConstraints = {

        @UniqueConstraint(
                name = "uk_author_name",
                columnNames = {"authorName"})

})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Author extends BaseEntity {


    @Column(name = "author_name", nullable = false, length = 100, unique = true)
    private String authorName;

    @Column(name = "author_bio", length = 500)
    private String authorBio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthorStatus status;

}
