package com.epam.esm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


@Data
@Entity
@Builder
@Table(name = "gift_certificates")
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificate {
    @Id
    @Column(name = "id",nullable = false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "certificate_name",nullable = false,unique = true)
    private String certificateName;

    @Column(name = "certificate_description",nullable = false)
    private String certificateDescription;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "duration",nullable = false)
    private int duration;

    @Column(name = "create_date",nullable = false)
    private Timestamp createDate;

    @Column(name = "last_update_date",nullable = false)
    private Timestamp lastUpdateDate;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "certificate_tag",
            joinColumns = @JoinColumn(name = "certificate_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id",nullable = false))
    private List<Tag> tags;

    public GiftCertificate(String certificateName, String certificateDescription, BigDecimal price, int duration, Timestamp createDate, Timestamp lastUpdateDate, List<Tag> tags) {
        this.certificateName = certificateName;
        this.certificateDescription = certificateDescription;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

}
