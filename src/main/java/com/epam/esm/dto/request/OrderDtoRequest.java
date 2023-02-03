package com.epam.esm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import static com.epam.esm.exception.ExceptionMessage.*;

@Data
public class OrderDtoRequest {

    @NotNull(message = NOT_VALID_USER_ID)
    private long userId;

    @NotNull(message = NOT_VALID_CERTIFICATE)
    private List<Long> certificateIds;
}
