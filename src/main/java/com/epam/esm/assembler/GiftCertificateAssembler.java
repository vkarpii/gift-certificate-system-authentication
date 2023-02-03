package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler implements RepresentationModelAssembler<GiftCertificateDtoResponse,GiftCertificateDtoResponse> {

    private final TagAssembler assembler;

    @Autowired
    public GiftCertificateAssembler(TagAssembler assembler) {
        this.assembler = assembler;
    }

    @Override
    public GiftCertificateDtoResponse toModel(GiftCertificateDtoResponse entity) {
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .find(entity.getId()))
                .withSelfRel());
        entity.setTags(assembler.toCollectionModel(entity.getTags()).getContent()
                .stream()
                .toList());
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .changePrice(entity.getId(),null))
                .withRel("change-price"));
        entity.add(linkTo(methodOn(GiftCertificateController.class)
                .changeDuration(entity.getId(),null))
                .withRel("change-duration"));
        return entity;
    }

    @Override
    public CollectionModel<GiftCertificateDtoResponse> toCollectionModel(Iterable<? extends GiftCertificateDtoResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
