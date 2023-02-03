package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.dto.response.OrderGiftCertificateResponse;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderGiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.util.IsoDateFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderMapper implements DtoMapper<OrderDtoResponse, OrderDtoRequest, Order> {

    private final IsoDateFormatter dateFormatter;

    private final UserMapper userMapper;


    @Autowired
    public OrderMapper(IsoDateFormatter dateFormatter, UserMapper userMapper) {
        this.dateFormatter = dateFormatter;
        this.userMapper = userMapper;
    }

    @Override
    public OrderDtoResponse toDTO(Order entity) {
        return OrderDtoResponse.builder()
                .id(entity.getId())
                .price(calculatePrice(entity))
                .user(userMapper.toDTO(entity.getUser()))
                .purchaseDate(dateFormatter.convertTimesTampToISOFormat(entity.getPurchaseDate()))
                .certificates(transform(entity.getCertificates()))
                .build();
    }

    private List<OrderGiftCertificateResponse> transform(List<OrderGiftCertificate> certificates){
        List<OrderGiftCertificateResponse> certificateResponse =  new ArrayList<>();
        certificates.forEach(certificate -> {
            certificateResponse.add(
                OrderGiftCertificateResponse.builder()
                        .name(certificate.getName())
                        .tags(certificate.getTags())
                        .id(certificate.getId())
                        .description(certificate.getDescription())
                        .endValidDate(dateFormatter.convertTimesTampToISOFormat(certificate.getEndValidDate()))
                        .startValidDate(dateFormatter.convertTimesTampToISOFormat(certificate.getStartValidDate()))
                        .price(certificate.getPrice())
                        .build()
            );
        });
        return certificateResponse;
    }

    private BigDecimal calculatePrice(Order order) {
        List<BigDecimal> prices = new ArrayList<>();
        order.getCertificates().forEach(certificate -> prices.add(certificate.getPrice()));
        return prices.stream().reduce(BigDecimal::add).orElseThrow(() -> {
            log.error(ExceptionMessage.ORDER_CREATING_ERROR);
            return new ApplicationException(ExceptionMessage.ORDER_CREATING_ERROR);
        });
    }

    public OrderDetailsDtoResponse toDetailsDTO(Order order) {
        return OrderDetailsDtoResponse.builder()
                .price(calculatePrice(order))
                .purchaseDate(dateFormatter.convertTimesTampToISOFormat(Timestamp.valueOf(LocalDateTime.now())))
                .build();
    }

    @Override
    public Order toEntity(OrderDtoRequest request) {
        return Order.builder()
                .user(User.builder().id(request.getUserId()).build())
                .build();
    }

}
