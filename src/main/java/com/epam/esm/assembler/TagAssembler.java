package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Tag;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class TagAssembler implements RepresentationModelAssembler<Tag,Tag> {

    @Override
    public Tag toModel(Tag entity) {
        if (entity.getLink("self").isEmpty()){
            entity.add(linkTo(methodOn(TagController.class)
                    .find(entity.getId()))
                    .withSelfRel());
        }
        return entity;
    }
}
