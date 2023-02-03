package com.epam.esm.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class OrderDetailsDtoResponse {
    private BigDecimal price;

    private String purchaseDate;

}
