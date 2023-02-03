package com.epam.esm.repository.certificate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<GiftCertificate,Long> {

    Optional<GiftCertificate> findByCertificateName(String certificateName);

    Page<GiftCertificate> findAll(Pageable pageable);

    @Query("SELECT certificate " +
            "FROM GiftCertificate certificate " +
            "JOIN certificate.tags tag " +
            "WHERE (:name is null  OR certificate.certificateName LIKE CONCAT('%',:name,'%')) AND " +
            "(:description is null OR certificate.certificateDescription LIKE CONCAT('%',:description,'%')) AND " +
            "(:size = 0 OR tag.tagName IN :tags) " +
            "GROUP BY certificate.id " +
            "HAVING COUNT(certificate.id) >= :size")
    Page<GiftCertificate> findAll(
            Pageable pageable,
            @Param("name") String name,
            @Param("description") String description,
            @Param("tags") List<String> tags,
            @Param("size") int size);
}
