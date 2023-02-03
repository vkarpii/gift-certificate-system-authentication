package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderGiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.IsoDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;

@Component
public class GiftCertificateMapper implements DtoMapper<GiftCertificateDtoResponse, GiftCertificateDtoRequest, GiftCertificate> {

    private final IsoDateFormatter dateFormatter;

    @Autowired
    public GiftCertificateMapper(IsoDateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public GiftCertificateDtoResponse toDTO(GiftCertificate certificate) {
        return GiftCertificateDtoResponse.builder()
                .id(certificate.getId())
                .name(certificate.getCertificateName())
                .description(certificate.getCertificateDescription())
                .price(certificate.getPrice())
                .tags(certificate.getTags())
                .createDate(dateFormatter.convertTimesTampToISOFormat(certificate.getCreateDate()))
                .lastUpdateDate(dateFormatter.convertTimesTampToISOFormat(certificate.getLastUpdateDate()))
                .durationInDays(certificate.getDuration())
                .build();
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDtoRequest certificateDto) {
        return GiftCertificate.builder()
                .certificateName(certificateDto.getName())
                .certificateDescription(certificateDto.getDescription())
                .price(certificateDto.getPrice())
                .duration(certificateDto.getDurationInDays())
                .tags(certificateDto.getTags().stream().map(Tag::new).collect(Collectors.toList()))
                .build();
    }

    public GiftCertificate merge(GiftCertificate mergedCertificate, GiftCertificate certificate) {
        return GiftCertificate.builder()
                .id(mergedCertificate.getId())
                .certificateName(requireNonNullElse(certificate.getCertificateName(),
                        mergedCertificate.getCertificateName()))
                .certificateDescription(requireNonNullElse(certificate.getCertificateDescription(),
                        mergedCertificate.getCertificateDescription()))
                .price(requireNonNullElse(certificate.getPrice(), mergedCertificate.getPrice()))
                .duration(certificate.getDuration())
                .createDate(requireNonNullElse(certificate.getCreateDate(), mergedCertificate.getCreateDate()))
                .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                .tags(requireNonNullElse(certificate.getTags(), mergedCertificate.getTags()))
                .build();
    }

    public List<OrderGiftCertificate> orderTransform(List<GiftCertificate> certificates){
        List<OrderGiftCertificate> orderCertificates = new ArrayList<>();
        certificates.forEach(certificate -> {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            orderCertificates.add(
                OrderGiftCertificate.builder()
                        .name(certificate.getCertificateName())
                        .description(certificate.getCertificateDescription())
                        .price(certificate.getPrice())
                        .startValidDate(timestamp)
                        .endValidDate(calculateEndDate(timestamp,certificate.getDuration()))
                        .tags(new ArrayList<>(certificate.getTags()))
                        .build()
            );
        });
        return orderCertificates;
    }

    private Timestamp calculateEndDate(Timestamp timestamp, int duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.add(Calendar.DAY_OF_WEEK,duration);
        return new Timestamp(calendar.getTime().getTime());
    }

    public List<GiftCertificate> toCertificatesWithIds(List<Long> certificateIds) {
        List<GiftCertificate> certificates = new ArrayList<>();
        certificateIds.forEach(certificateId -> certificates.add(GiftCertificate.builder().id(certificateId).build()));
        return certificates;
    }
}

