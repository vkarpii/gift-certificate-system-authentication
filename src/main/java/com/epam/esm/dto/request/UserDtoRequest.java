package com.epam.esm.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDtoRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String login;

    private String password;

}
