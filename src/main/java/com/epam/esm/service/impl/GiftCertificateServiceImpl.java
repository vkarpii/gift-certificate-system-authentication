package com.epam.esm.service.impl;

import com.epam.esm.dto.mapper.impl.GiftCertificateMapper;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationException;
import com.epam.esm.exception.ExceptionMessage;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.util.QueryBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final QueryBuilder builder;
    private final CertificateRepository repository;

    private final GiftCertificateMapper certificateMapper;
    private final TagService tagService;

    private final MessageSource messageSource;

    @Autowired
    public GiftCertificateServiceImpl(QueryBuilder builder, CertificateRepository repository, GiftCertificateMapper certificateMapper, TagService tagService, MessageSource messageSource) {
        this.builder = builder;
        this.repository = repository;
        this.certificateMapper = certificateMapper;
        this.tagService = tagService;
        this.messageSource = messageSource;
    }

    /*@PostConstruct
    public void generateCertificates() throws Exception {
        log.info("Started generate CERTIFICATES...");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> response = getResponse(1000);
        List<String> names = response.getBody();
        List<Tag> allTags = IntStream.rangeClosed(1,1000)
                .mapToObj(index -> Tag.builder()
                        .tagName(names.get(index - 1))
                        .build()).toList();
        response = getResponse(10000);
        List<String> namesCertificate = response.getBody();
        response = getResponse(10000);
        List<String> descriptions = response.getBody();
        log.info("Tags get!");
        Random random = new Random();
        List<GiftCertificate> certificates = IntStream.rangeClosed(1,10000)
                .mapToObj(index -> {
                    List<Tag> tagsForCertificate = new ArrayList<>();
                    for (int i = 0;i != 3;i++){
                        tagsForCertificate.add(allTags.get(random.nextInt(999)));
                    }
                    //log.info("Create CERTIFICATE#"+index);
                    return GiftCertificate.builder()
                            .certificateName(namesCertificate.get(index - 1))
                            .certificateDescription(descriptions.get(index - 1))
                            .createDate(Timestamp.valueOf(LocalDateTime.now()))
                            .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                            .price(random(99999))
                            .duration(getRandomNumber(1,100))
                            .tags(tagsForCertificate)
                            .build();
                }).toList();
        log.info("Push to database!");
        repository.saveAll(certificates);
        log.info("CERTIFICATES generated!!!");
    }

    private ResponseEntity getResponse(int number){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(
                "https://random-word-api.herokuapp.com/word?number="+number,
                List.class
        );
    }
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static BigDecimal random(int range) {
        BigDecimal max = new BigDecimal(range);
        BigDecimal randFromDouble = new BigDecimal(Math.random());
        BigDecimal actualRandomDec = randFromDouble.multiply(max);
        actualRandomDec = actualRandomDec
                .setScale(2, BigDecimal.ROUND_DOWN);
        return actualRandomDec;
    }*/

    private GiftCertificate getCertificateById(long id){
        return repository.findById(id).orElseThrow(() -> {
            log.error(messageSource.getMessage(ExceptionMessage.CERTIFICATE_NOT_FOUND, new Object[]{},
                    LocaleContextHolder.getLocale()));
            return new ApplicationException(ExceptionMessage.CERTIFICATE_NOT_FOUND);
        });
    }

    @Override
    @Transactional
    public GiftCertificateDtoResponse getCertificateDtoById(long id) {
        return certificateMapper.toDTO(getCertificateById(id));
    }

    @Override
    @Transactional
    public GiftCertificateDtoResponse createNewCertificate(GiftCertificateDtoRequest certificateDTO) {
        GiftCertificate certificate = certificateMapper.toEntity(certificateDTO);
        if (repository.findByCertificateName(certificate.getCertificateName()).isPresent()) {
            log.error(messageSource.getMessage(ExceptionMessage.CERTIFICATE_IS_ALREADY_EXISTS, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.CERTIFICATE_IS_ALREADY_EXISTS);
        }
        setNewCreateDate(certificate);
        setNewLastUpdateDate(certificate);
        mergeTagsWithDb(certificate);
        GiftCertificate createdCertificate = repository.save(certificate);
        return certificateMapper.toDTO(createdCertificate);
    }

    @Override
    public GiftCertificateDtoResponse updateCertificate(long id, GiftCertificateDtoRequest certificateDto) {
        GiftCertificate certificate = certificateMapper.toEntity(certificateDto);
        GiftCertificate current = getCertificateById(id);
        mergeTagsWithDb(certificate);
        current = certificateMapper.merge(current, certificate);
        repository.save(current);
        return certificateMapper.toDTO(current);
    }

    private void mergeTagsWithDb(GiftCertificate certificate){
        if(nonNull(certificate.getTags())){
            List<Tag> tags = tagService.getFullTagsData(certificate.getTags());
            certificate.setTags(tags);
        }
    }

    private void setNewLastUpdateDate(GiftCertificate certificate) {
        certificate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    private void setNewCreateDate(GiftCertificate certificate) {
        certificate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public List<GiftCertificateDtoResponse> getCertificates(Pageable pageable, Map<String, String> params) {
        List<String> tagNames = builder.buildTagNamesList(params);
        List<GiftCertificate> giftCertificate = repository.findAll(
                builder.buildPageableWithSort(pageable,params),
                builder.getCertificateName(params),
                builder.getCertificateDescription(params),
                tagNames, tagNames.size()).getContent();
        return giftCertificate.stream().map(certificateMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteCertificateById(long id) {
        repository.delete(getCertificateById(id));
        return true;
    }

    @Override
    public List<GiftCertificate> getFullCertificatesData(List<GiftCertificate> certificates) {
        List<GiftCertificate> updated = new ArrayList<>();
        certificates.forEach(certificate -> updated.add(getCertificateById(certificate.getId())));
        return updated;
    }

    @Override
    public GiftCertificateDtoResponse changePrice(long id, GiftCertificateDtoRequest request) {
        BigDecimal price = request.getPrice();
        if (price.compareTo(BigDecimal.ZERO) < 0){
            log.error(messageSource.getMessage(ExceptionMessage.WRONG_PRICE, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.WRONG_PRICE);
        }
        GiftCertificate certificate = getCertificateById(id);
        certificate.setPrice(price);
        return certificateMapper.toDTO(repository.save(certificate));
    }
    @Override
    public GiftCertificateDtoResponse changeDuration(long id, GiftCertificateDtoRequest request) {
        int duration = request.getDurationInDays();
        if (duration <= 0){
            log.error(messageSource.getMessage(ExceptionMessage.WRONG_DURATION, new Object[]{},
                    LocaleContextHolder.getLocale()));
            throw new ApplicationException(ExceptionMessage.WRONG_DURATION);
        }
        GiftCertificate certificate = getCertificateById(id);
        certificate.setDuration(duration);
        return certificateMapper.toDTO(repository.save(certificate));
    }

}
