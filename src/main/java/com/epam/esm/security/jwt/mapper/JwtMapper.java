package com.epam.esm.security.jwt.mapper;

public interface JwtMapper<K,G> {
    K toUserDetails(G entity);
}
