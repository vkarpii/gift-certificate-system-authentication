package com.epam.esm.service;

import com.epam.esm.dto.request.GiftCertificateDtoRequest;
import com.epam.esm.dto.response.GiftCertificateDtoResponse;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ApplicationException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * This interface represents Service implementation that connected controller with Data Access Object.
 *
 * @param <T> has to implement {@link GiftCertificate} interface
 * @author Vitaly Karpii
 * @see GiftCertificate
 */
public interface GiftCertificateService {

    /**
     * This method return gift certificate by his id.
     *
     * @return {@link GiftCertificateDtoResponse}
     * @throws {@link ApplicationException} in case if tag not found with searched id.
     */
    GiftCertificateDtoResponse getCertificateDtoById(long id);

    /**
     * This method create new gift certificate.
     *
     * @throws {@link ApplicationException} in case if gift certificate's name is not correct.
     */
    GiftCertificateDtoResponse createNewCertificate(GiftCertificateDtoRequest certificate);

    /**
     * This method return all active gift certificates with criteria.
     *
     * @return list of{@link GiftCertificateDtoResponse}
     */
    List<GiftCertificateDtoResponse> getCertificates(Pageable pageable, Map<String, String> params);

    /**
     * This method delete gift certificate by his id.
     *
     * @throws {@link ApplicationException} in case if this gift certificate's id not found.
     */
    boolean deleteCertificateById(long id);

    /**
     * This method update gift certificate.
     *
     * @throws {@link ApplicationException} in case if this gift certificate's id not found.
     */
    GiftCertificateDtoResponse updateCertificate(long id, GiftCertificateDtoRequest certificate);

    /**
     * This method update gift certificate price.
     *
     * @throws {@link ApplicationException} in case if this gift certificate's id not found.
     */
    GiftCertificateDtoResponse changePrice(long id, GiftCertificateDtoRequest request);

    /**
     * This method update gift certificate duration.
     *
     * @throws {@link ApplicationException} in case if this gift certificate's id not found.
     */
    GiftCertificateDtoResponse changeDuration(long id, GiftCertificateDtoRequest request);

    /**
     * This method update all existing gift certificates by id.
     *
     * @throws {@link ApplicationException} in case if this gift certificate's id not found.
     */
    List<GiftCertificate> getFullCertificatesData(List<GiftCertificate> certificates);

}
