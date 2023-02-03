package com.epam.esm.repository.order;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findAllByUserId(long user_id, Pageable pageable);

    @Query("SELECT tag " +
            "FROM Order order " +
            "JOIN order.certificates certificate " +
            "JOIN certificate.tags tag " +
            "WHERE order.id = ( " +
                    "SELECT order.id " +
                    "FROM Order order " +
                    "JOIN order.user user " +
                    "JOIN order.certificates certificate " +
                    "WHERE user.id = :id " +
                    "GROUP BY order.purchaseDate " +
                    "ORDER BY SUM(certificate.price) DESC " +
                    "LIMIT 1 ) "+
            "GROUP BY tag.tagName " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 1")
    Optional<Tag> mostWidelyUsedTag(long id);
}
