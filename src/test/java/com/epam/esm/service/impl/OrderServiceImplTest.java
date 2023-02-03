package com.epam.esm.service.impl;


import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.mapper.impl.OrderMapper;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.dto.response.UserDtoResponse;
import com.epam.esm.entity.*;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    private final long DEFAULT_ID = 1;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserService userService;

    @Mock
    private GiftCertificateService certificateService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private GiftCertificateMapper certificateMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void getOrderDtoByIdTest(){
        Mockito.when(orderRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(new Order()));
        Mockito.when(orderMapper.toDTO(Mockito.any())).thenReturn(OrderDtoResponse.builder().build());
        OrderDtoResponse response = orderService.getOrderDtoById(DEFAULT_ID);
        assertNotNull(response);
    }

    @Test
    void getOrdersDetailsByIdTest(){
        Order order = new Order();
        order.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
        //order.setDetails(new OrderDetails(BigDecimal.ONE, Timestamp.valueOf(LocalDateTime.now())));
        Mockito.when(orderRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(order));
        OrderDetailsDtoResponse dtoResponse = OrderDetailsDtoResponse.builder()
                .price(BigDecimal.ONE).build();
        Mockito.when(orderMapper.toDetailsDTO(Mockito.any())).thenReturn(dtoResponse);
        OrderDetailsDtoResponse response = orderService.getOrderDetailsById(DEFAULT_ID);
        assertNotNull(response.getPrice());
    }

    @Test
    void mostWidelyUsedTagTest(){
        Mockito.when(userService.getUserById(DEFAULT_ID)).thenReturn(UserDtoResponse.builder().build());
        Mockito.when(orderRepository.mostWidelyUsedTag(DEFAULT_ID)).thenReturn(Optional.of(new Tag()));
        Tag tag = orderService.mostWidelyUsedTag(DEFAULT_ID);
    }

    @Test
    void mostWidelyUsedTagShouldThrowException(){
        Mockito.when(userService.getUserById(DEFAULT_ID)).thenReturn(UserDtoResponse.builder().build());
        Mockito.when(orderRepository.mostWidelyUsedTag(DEFAULT_ID)).thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () -> orderService.mostWidelyUsedTag(DEFAULT_ID));
    }

    @Test
    void getOrderDtoByIdShouldThrowException(){
        Mockito.when(orderRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () -> orderService.getOrderDtoById(DEFAULT_ID));
    }

    @Test
    void getOrderDetailsByIdTest(){
        Pageable pageable = PageRequest.of(0,10);

        Mockito.when(userService.getUserById(DEFAULT_ID)).thenReturn(UserDtoResponse.builder().build());
        Mockito.when(orderRepository.findAllByUserId(DEFAULT_ID,pageable))
                .thenReturn(new PageImpl<>(List.of(new Order())));
        List<OrderDtoResponse> orders = orderService.getAllOrders(DEFAULT_ID,pageable);
        assertNotNull(orders);
    }

    @Test
    void createNewOrderTest(){
        OrderDtoRequest orderDtoRequest = new OrderDtoRequest();
        orderDtoRequest.setCertificateIds(List.of(DEFAULT_ID));
        orderDtoRequest.setUserId(DEFAULT_ID);
        Mockito.when(certificateService.getFullCertificatesData(Mockito.any()))
                .thenReturn(List.of(new GiftCertificate()));
        OrderGiftCertificate orderGiftCertificate = new OrderGiftCertificate();
        orderGiftCertificate.setPrice(BigDecimal.ONE);
        orderGiftCertificate.setId(DEFAULT_ID);
        Mockito.when(certificateMapper.orderTransform(Mockito.any())).thenReturn(List.of(orderGiftCertificate));
        User user = new User();
        user.setId(DEFAULT_ID);
        Mockito.when(userService.getUser(DEFAULT_ID)).thenReturn(user);
        Order order = new Order();
        order.setCertificates(List.of(orderGiftCertificate));
        order.setUser(user);
        Mockito.when(orderMapper.toEntity(orderDtoRequest)).thenReturn(order);
        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order);
        Mockito.when(orderMapper.toDTO(Mockito.any())).thenReturn(OrderDtoResponse.builder().build());
        OrderDtoResponse response = orderService.createNewOrder(orderDtoRequest);
        assertNotNull(response);
    }

}
