package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.request.UserDtoRequest;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapper<UserDtoResponse, UserDtoRequest, User> {

    private final PasswordEncoder encoder;

    @Autowired
    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public UserDtoResponse toDTO(User user) {
        return UserDtoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .login(user.getLogin())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public User toEntity(UserDtoRequest userDtoRequest) {
        return User.builder()
                .firstName(userDtoRequest.getFirstName())
                .lastName(userDtoRequest.getLastName())
                .email(userDtoRequest.getEmail())
                .login(userDtoRequest.getLogin())
                .password(encoder.encode(userDtoRequest.getPassword()))
                .role(new Role("User"))
                .build();
    }
}
