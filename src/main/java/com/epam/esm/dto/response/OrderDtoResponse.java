package com.epam.esm.dto.response;

import com.epam.esm.entity.OrderGiftCertificate;
import com.epam.esm.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class OrderDtoResponse extends RepresentationModel<OrderDtoResponse> {

    private long id;

    private BigDecimal price;

    private String purchaseDate;

    private UserDtoResponse user;

    private List<OrderGiftCertificateResponse> certificates;
}
