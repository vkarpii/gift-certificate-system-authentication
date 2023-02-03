package com.epam.esm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.esm.exception.ExceptionMessage.*;

@Data
@Builder
public class GiftCertificateDtoRequest {
    @NotBlank(message = NOT_VALID_CERTIFICATE_NAME)
    private String name;

    @NotBlank(message = NOT_VALID_CERTIFICATE_DESCRIPTION)
    private String description;

    @NotNull(message = NOT_VALID_CERTIFICATE_PRICE)
    private BigDecimal price;

    @NotNull(message = NOT_VALID_CERTIFICATE_TAGS)
    private List<String> tags;

    @NotNull(message = NOT_VALID_CERTIFICATE_DURATION)
    private int durationInDays;
}
