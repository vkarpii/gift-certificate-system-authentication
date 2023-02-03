package com.epam.esm.dto.mapper;

public interface DtoMapper<T,G, K> {

    T toDTO(K entity);

    K toEntity(G request);

}
