package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "order_gift_certificates")
@NoArgsConstructor
@AllArgsConstructor
public class OrderGiftCertificate {
    @Id
    @Column(name = "id",nullable = false,unique = true)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "name",nullable = false,unique = true)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "start_valid_date",nullable = false)
    private Timestamp startValidDate;

    @Column(name = "end_valid_date",nullable = false)
    private Timestamp endValidDate;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "order_certificate_tag",
            joinColumns = @JoinColumn(name = "certificate_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id",nullable = false)
    )
    private List<Tag> tags;

    public OrderGiftCertificate(String name, String description, BigDecimal price, Timestamp startValidDate, Timestamp endValidDate, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.startValidDate = startValidDate;
        this.endValidDate = endValidDate;
        this.tags = tags;
    }
}
