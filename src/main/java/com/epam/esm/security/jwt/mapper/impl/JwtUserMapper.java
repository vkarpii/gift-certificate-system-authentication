package com.epam.esm.security.jwt.mapper.impl;

import com.epam.esm.entity.User;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.mapper.JwtMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUserMapper implements JwtMapper<UserDetails,User> {
    @Override
    public UserDetails toUserDetails(User entity) {
        return JwtUser.builder()
                .id(entity.getId())
                .role(entity.getRole())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .password(entity.getPassword())
                .login(entity.getLogin())
                .build();
    }
}
