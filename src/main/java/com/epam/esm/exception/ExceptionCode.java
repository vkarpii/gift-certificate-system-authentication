package com.epam.esm.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    TAG_IS_ALREADY_EXISTS("409-01", ExceptionMessage.TAG_IS_ALREADY_EXISTS),
    TAG_NOT_FOUND("404-01", ExceptionMessage.TAG_NOT_FOUND),
    CERTIFICATE_NOT_FOUND("404-02", ExceptionMessage.CERTIFICATE_NOT_FOUND),
    CERTIFICATE_IS_ALREADY_EXISTS("409-02", ExceptionMessage.CERTIFICATE_IS_ALREADY_EXISTS),
    BAD_ATTRIBUTES("400-01", ExceptionMessage.BAD_ATTRIBUTES),
    USER_NOT_FOUND("404-03",ExceptionMessage.USER_NOT_FOUND),
    ORDER_NOT_FOUND("404-04",ExceptionMessage.ORDER_NOT_FOUND),
    BAD_REQUEST("400-00",ExceptionMessage.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("500-00",ExceptionMessage.INTERNAL_SERVER_ERROR),
    ORDER_CREATING_ERROR("409-03",ExceptionMessage.ORDER_CREATING_ERROR),
    WRONG_PRICE("412-01",ExceptionMessage.WRONG_PRICE),
    WRONG_DURATION("412-02",ExceptionMessage.WRONG_DURATION),
    WRONG_SIZE("412-03",ExceptionMessage.WRONG_SIZE),
    WRONG_PAGE("412-04",ExceptionMessage.WRONG_PAGE),
    USER_WITHOUT_ORDER(ORDER_NOT_FOUND.getCode(), ExceptionMessage.USER_WITHOUT_ORDER),
    NOT_VALID_TAG_NAME("400-02",ExceptionMessage.NOT_VALID_TAG_NAME),
    NOT_VALID_CERTIFICATE_NAME("400-03",ExceptionMessage.NOT_VALID_CERTIFICATE_NAME),
    NOT_VALID_CERTIFICATE_DESCRIPTION("400-04",ExceptionMessage.NOT_VALID_CERTIFICATE_DESCRIPTION),
    NOT_VALID_CERTIFICATE_DURATION("400-05",ExceptionMessage.NOT_VALID_CERTIFICATE_DURATION),
    NOT_VALID_CERTIFICATE_PRICE("400-06",ExceptionMessage.NOT_VALID_CERTIFICATE_PRICE),
    NOT_VALID_CERTIFICATE_TAGS("400-07",ExceptionMessage.NOT_VALID_CERTIFICATE_TAGS),
    NOT_VALID_CERTIFICATE("400-08",ExceptionMessage.NOT_VALID_CERTIFICATE),
    NOT_VALID_USER_ID("400-09",ExceptionMessage.NOT_VALID_USER_ID),
    USER_ID_EXIST("409-03",ExceptionMessage.USER_ID_EXIST);
    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getCodeByMessage(String message) {
        String code = null;
        ExceptionCode[] enums = ExceptionCode.values();
        for (ExceptionCode element : enums) {
            if (element.getMessage().equals(message)) {
                code = element.getCode();
            }
        }
        return code;
    }

}
