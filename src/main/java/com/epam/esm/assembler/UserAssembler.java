package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.request.OrderDtoRequest;
import com.epam.esm.dto.response.UserDtoResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements RepresentationModelAssembler<UserDtoResponse,UserDtoResponse> {

    @Override
    public UserDtoResponse toModel(UserDtoResponse entity) {
        entity.add(linkTo(methodOn(UserController.class)
                .find(entity.getId()))
                .withSelfRel());
        entity.add(linkTo(methodOn(OrderController.class)
                .find(entity.getId()))
                .withRel("all-orders"));
        entity.add(linkTo(methodOn(OrderController.class)
                .mostWidelyUsedTag(entity.getId()))
                .withRel("most-widely-used-tag"));
        entity.add(linkTo(methodOn(OrderController.class)
                .create(new OrderDtoRequest()))
                .withRel("make-order"));
        return entity;
    }
}
