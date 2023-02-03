package com.epam.esm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.epam.esm.exception.ExceptionMessage.NOT_VALID_TAG_NAME;

@Data
@Entity
@Table(schema = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @Column(name = "name",nullable = false,unique = true)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
