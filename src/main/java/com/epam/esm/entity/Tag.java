package com.epam.esm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

import static com.epam.esm.exception.ExceptionMessage.*;


@Data
@Entity
@Table(name = "tags")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends RepresentationModel<Tag> {
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = NOT_VALID_TAG_NAME)
    @Column(name = "tag_name",nullable = false,unique = true)
    private String tagName;

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        return 31 * (tagName != null ? tagName.hashCode() : 0);
    }

}
