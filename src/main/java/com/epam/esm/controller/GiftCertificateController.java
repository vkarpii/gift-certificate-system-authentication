package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.service.GiftCertificateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/gift-certificate")
public class GiftCertificateController {
    private final GiftCertificateService service;
    private final GiftCertificateAssembler assembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService service, GiftCertificateAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public GiftCertificateDtoResponse find(@PathVariable long id) {
        return assembler.toModel(service.getCertificateDtoById(id));
    }

    @GetMapping
    public List<GiftCertificateDtoResponse> findAll(
            @PageableDefault Pageable pageable,
            @RequestParam Map<String, String> params
    ) {
        return assembler.toCollectionModel(service.getCertificates(pageable,params))
                .getContent()
                .stream()
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping
    public GiftCertificateDtoResponse create(@RequestBody @Valid GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.createNewCertificate(certificateDto));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.deleteCertificateById(id);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PutMapping("/{id}")
    public GiftCertificateDtoResponse update(@PathVariable long id,
                                             @RequestBody GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.updateCertificate(id, certificateDto));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PutMapping("/price/{id}")
    public GiftCertificateDtoResponse changePrice(@PathVariable long id,
                                                  @RequestBody GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.changePrice(id, certificateDto));
    }


    @PreAuthorize("hasAuthority('Admin')")
    @PutMapping("/duration/{id}")
    public GiftCertificateDtoResponse changeDuration(@PathVariable long id,
                                                     @RequestBody GiftCertificateDtoRequest certificateDto) {
        return assembler.toModel(service.changeDuration(id, certificateDto));
    }
}
