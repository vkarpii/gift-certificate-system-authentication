package com.epam.esm.service.impl;


import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.util.QueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    private final long DEFAULT_ID = 1;

    private final String TEST_DATA = "Test";

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TagService tagService;

    @Mock
    private GiftCertificateMapper mapper;

    @Spy
    private QueryBuilder builder =
            new QueryBuilder();

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void findGiftCertificateByIdTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(DEFAULT_ID);
        giftCertificate.setLastUpdateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));
        giftCertificate.setCreateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));

        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(giftCertificate));
        GiftCertificateDtoResponse response = giftCertificateService.getCertificateDtoById(DEFAULT_ID);

        Assertions.assertEquals(mapper.toDTO(giftCertificate), response);
    }

    @Test
    void findGiftCertificateByIdShouldThrowException() {
        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> giftCertificateService.getCertificateDtoById(DEFAULT_ID));
    }

    @Test
    void deleteGiftCertificateTest() {
        GiftCertificate certificate = GiftCertificate.builder().id(DEFAULT_ID).build();
        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(certificate));

        Assertions.assertEquals(Boolean.TRUE, giftCertificateService.deleteCertificateById(DEFAULT_ID));
    }

    @Test
    void getFullCertificatesDataTest() {
        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setLastUpdateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));
        giftCertificate.setCreateDate(Timestamp.valueOf("2022-10-10 13:02:11.0"));
        giftCertificateList.add(giftCertificate);
        giftCertificateList.add(giftCertificate);

        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(giftCertificate));
        List<GiftCertificate> resultGiftCertificateDtoList = giftCertificateService.getFullCertificatesData(giftCertificateList);

        Assertions.assertEquals(2, resultGiftCertificateDtoList.size());
    }

    @Test
    void createNewCertificateTest() {
        GiftCertificateDtoRequest certificate = GiftCertificateDtoRequest.builder()
                .name("Tested").build();

        GiftCertificate giftCertificate = GiftCertificate.builder()
                .certificateName(certificate.getName()).build();

        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(giftCertificate);
        Mockito.when(certificateRepository.findByCertificateName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(certificateRepository.save(Mockito.any())).thenReturn(giftCertificate);
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(GiftCertificateDtoResponse.builder().build());
        GiftCertificateDtoResponse response= giftCertificateService.createNewCertificate(certificate);

        assertNotNull(response);
    }

    @Test
    void createNewCertificateShouldThrowException() {
        GiftCertificateDtoRequest certificate = GiftCertificateDtoRequest.builder()
                .name("Tested")
                .build();
        GiftCertificate giftCertificate = GiftCertificate.builder().certificateName(certificate.getName()).build();
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(giftCertificate);
        Mockito.when(certificateRepository.findByCertificateName(Mockito.any())).thenReturn(Optional.of(giftCertificate));

        assertThrows(ApplicationException.class, () -> giftCertificateService.createNewCertificate(certificate));
    }

    @Test
    void updateGiftCertificateTest() {
        GiftCertificateDtoRequest request = GiftCertificateDtoRequest.builder()
                .name("Test")
                .price(BigDecimal.ONE)
                .description("Desc")
                .durationInDays(12)
                .build();
        GiftCertificate certificate = GiftCertificate.builder()
                .certificateName(request.getName())
                .tags(List.of(Tag.builder().build()))
                .build();
        GiftCertificateDtoResponse response = GiftCertificateDtoResponse.builder()
                .name(request.getName())
                .build();
        Mockito.when(mapper.toEntity(request)).thenReturn(certificate);
        Mockito.when(certificateRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(certificate));
        Mockito.when(mapper.merge(Mockito.any(),Mockito.any())).thenReturn(certificate);
        Mockito.when(mapper.toDTO(certificate)).thenReturn(response);
        GiftCertificateDtoResponse resultGifCertificateDto = giftCertificateService.updateCertificate
                (DEFAULT_ID, request);

        Assertions.assertEquals(request.getName(), resultGifCertificateDto.getName());
    }

    @Test
    void changePriceTest() {
        GiftCertificateDtoResponse certificateResponse = GiftCertificateDtoResponse.builder()
                .price(BigDecimal.ONE)
                .build();
        GiftCertificate certificate = GiftCertificate.builder()
                .price(certificateResponse.getPrice()).build();
        Mockito.when(certificateRepository.findById(Mockito.any())).thenReturn(Optional.of(certificate));
        Mockito.when(certificateRepository.save(Mockito.any())).thenReturn(certificate);
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(certificateResponse);
        GiftCertificateDtoRequest request = GiftCertificateDtoRequest.builder()
                .price(BigDecimal.ONE).build();
        GiftCertificateDtoResponse response = giftCertificateService.changePrice(DEFAULT_ID, request);
        Assertions.assertEquals(BigDecimal.ONE, response.getPrice());
    }

    @Test
    void changeDurationTest() {
        GiftCertificateDtoResponse certificateResponse = GiftCertificateDtoResponse.builder()
                .durationInDays(12)
                .build();
        GiftCertificate certificate = GiftCertificate.builder()
                .duration(certificateResponse.getDurationInDays())
                .build();
        Mockito.when(certificateRepository.findById(Mockito.any())).thenReturn(Optional.of(certificate));
        Mockito.when(certificateRepository.save(Mockito.any())).thenReturn(certificate);
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(certificateResponse);
        GiftCertificateDtoRequest request = GiftCertificateDtoRequest.builder()
                .durationInDays(certificate.getDuration())
                .build();
        GiftCertificateDtoResponse response = giftCertificateService.changeDuration(DEFAULT_ID,request);
        Assertions.assertEquals(request.getDurationInDays(), response.getDurationInDays());
    }

    @Test
    void changeDurationShouldThrowException() {
        assertThrows(ApplicationException.class, () -> {
            GiftCertificateDtoRequest request = GiftCertificateDtoRequest.builder()
                    .durationInDays(-1).build();
            giftCertificateService.changeDuration(DEFAULT_ID,request);
        });
    }

    @Test
    void changePriceShouldThrowException() {
        assertThrows(ApplicationException.class, () ->
        {
            GiftCertificateDtoRequest request = GiftCertificateDtoRequest.builder()
                    .price(BigDecimal.valueOf(-1)).build();
            giftCertificateService.changePrice(DEFAULT_ID,request);
        });
    }

    @Test
    void getCertificatesWithPaginationTest(){
        Pageable pageable = PageRequest.of(0,10);
        Map<String,String> params = new HashMap<>();
        params.put("giftCertificateName",TEST_DATA);
        params.put("tagName",TEST_DATA);
        params.put("giftCertificateDescription",TEST_DATA);
        Mockito.when(certificateRepository.findAll(pageable, TEST_DATA,TEST_DATA, List.of(TEST_DATA),1))
                .thenReturn(new PageImpl<>(List.of(GiftCertificate.builder().build())));
        Mockito.when(mapper.toDTO(Mockito.any())).thenReturn(GiftCertificateDtoResponse.builder().build());
        List<GiftCertificateDtoResponse> certificates= giftCertificateService.getCertificates(pageable,params);
        assertNotNull(certificates);
    }

    @Test
    void deleteGiftGiftCertificateTestShouldThrowException() {
        int id = 99;
        assertThrows(ApplicationException.class, () -> giftCertificateService.deleteCertificateById(id));
    }
}
