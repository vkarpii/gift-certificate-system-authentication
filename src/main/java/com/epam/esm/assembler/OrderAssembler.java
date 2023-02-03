package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.response.OrderDtoResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements RepresentationModelAssembler<OrderDtoResponse,OrderDtoResponse> {

    private final TagAssembler assembler;

    public OrderAssembler(TagAssembler assembler) {
        this.assembler = assembler;
    }

    @Override
    public OrderDtoResponse toModel(OrderDtoResponse entity) {
        entity.add(linkTo(methodOn(OrderController.class)
                .find(entity.getId()))
                .withSelfRel());
        entity.add(linkTo(methodOn(OrderController.class)
                .findDetails(entity.getId()))
                .withRel("orderDetails"));
        entity.getCertificates().forEach(certificate ->
                assembler.toCollectionModel(certificate.getTags()));
        return entity;
    }
}
