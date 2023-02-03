package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.OrderDetailsDtoResponse;
import com.epam.esm.dto.response.OrderDtoResponse;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService service;
    private final OrderAssembler assembler;

    @Autowired
    public OrderController(OrderService service, OrderAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PreAuthorize("hasAnyAuthority('User','Admin')")
    @PostMapping
    public OrderDtoResponse create(@RequestBody @Valid OrderDtoRequest order) {
        return assembler.toModel(service.createNewOrder(order));
    }

    @PreAuthorize("hasAnyAuthority('User','Admin')")
    @GetMapping("/{id}")
    public OrderDtoResponse find(@PathVariable long id) {
        return assembler.toModel(service.getOrderDtoById(id));
    }

    @PreAuthorize("hasAnyAuthority('User','Admin')")
    @GetMapping("/details/{id}")
    public OrderDetailsDtoResponse findDetails(@PathVariable long id) {
        return service.getOrderDetailsById(id);
    }

    @PreAuthorize("hasAnyAuthority('User','Admin')")
    @GetMapping(value = "/user/{id}")
    public List<OrderDtoResponse> findAll(@PathVariable long id, Pageable pageable) {
        return assembler.toCollectionModel(service.getAllOrders(id,pageable))
                .getContent()
                .stream()
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('User','Admin')")
    @GetMapping(value = "/top-tag/user/{id}")
    public Tag mostWidelyUsedTag(@PathVariable long id){
        return service.mostWidelyUsedTag(id);
    }
}
