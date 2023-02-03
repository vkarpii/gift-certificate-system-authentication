package com.epam.esm.controller;

import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService service;
    private final TagAssembler assembler;

    @Autowired
    public TagController(TagService service, TagAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public Tag create(@RequestBody @Valid Tag tag) {
        return assembler.toModel(service.createNewTag(tag));
    }

    @PreAuthorize("hasAnyAuthority('Admin','User')")
    @GetMapping("/{id}")
    public Tag find(@PathVariable long id) {
        return assembler.toModel(service.getTagById(id));
    }

    @GetMapping
    public List<Tag> findAll(@PageableDefault Pageable pageable) {
        return assembler.toCollectionModel(service.getAllTags(pageable))
                .getContent().stream().toList();
    }

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.deleteTagById(id);
    }
}
