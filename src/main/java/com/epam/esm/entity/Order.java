package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @Column(name = "id",nullable = false,unique = true)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "purchase_date",nullable = false)
    private Timestamp purchaseDate;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false)
    private User user;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "certificate_order",
            joinColumns = @JoinColumn(name = "order_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "certificate_id",nullable = false)
    )
    private List<OrderGiftCertificate> certificates;

    public Order(Timestamp purchaseDate, User user, List<OrderGiftCertificate> certificates) {
        this.purchaseDate = purchaseDate;
        this.user = user;
        this.certificates = certificates;
    }
}
