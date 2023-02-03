package com.epam.esm.dto.response;

import com.epam.esm.entity.Tag;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
public class UserDtoResponse extends RepresentationModel<Tag> {
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String login;

    private String role;
}
