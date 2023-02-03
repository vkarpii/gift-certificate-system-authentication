package com.epam.esm.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class QueryBuilder {

    private static final String PARAM_TAG_NAME = "tagName";
    private static final String PARAM_ORDER = "order";
    private static final String PARAM_GIFT_CERTIFICATE_NAME = "giftCertificateName";
    private static final String PARAM_GIFT_CERTIFICATE_DESCRIPTION = "giftCertificateDescription";
    private static final String DESC_DIRECTION = "desc";
    private static final String TAG_SPLITTER = ",";
    private static final String ORDER_SPLITTER = ":";

    public Pageable buildPageableWithSort(Pageable pageable,Map<String, String> params){
        String order = params.get(PARAM_ORDER);
        if (Objects.nonNull(order) && order.contains(ORDER_SPLITTER)){
            String param = order.split(ORDER_SPLITTER)[0];
            String direction = order.split(ORDER_SPLITTER)[1];
            Sort sort = Sort.by(getDirection(direction),param);
            return PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        return pageable;
    }

    private Sort.Direction getDirection(String direction){
        if (direction.equalsIgnoreCase(DESC_DIRECTION)){
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public String getCertificateName(Map<String, String> params){
        return params.get(PARAM_GIFT_CERTIFICATE_NAME);
    }

    public String getCertificateDescription(Map<String, String> params){
        return params.get(PARAM_GIFT_CERTIFICATE_DESCRIPTION);
    }

    public List<String> buildTagNamesList(Map<String, String> params){
        List<String> tags = new ArrayList<>();
        if (Objects.nonNull(params.get(PARAM_TAG_NAME))){
            tags = Arrays.asList(params.get(PARAM_TAG_NAME).split(TAG_SPLITTER));
        }
        return tags;
    }
}
