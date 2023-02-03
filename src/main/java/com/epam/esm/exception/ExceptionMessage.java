package com.epam.esm.exception;

public final class ExceptionMessage {
    public final static String TAG_IS_ALREADY_EXISTS = "error.message.tag.is.already.exists";
    public final static String TAG_NOT_FOUND = "error.message.tag.not.found";
    public static final String CERTIFICATE_NOT_FOUND = "error.message.certificate.not.found";
    public static final String CERTIFICATE_IS_ALREADY_EXISTS = "error.message.certificate.is.already.exists";
    public static final String BAD_ATTRIBUTES = "error.message.bad.attributes";
    public static final String USER_NOT_FOUND = "error.message.user.not.found";
    public static final String ORDER_NOT_FOUND = "error.message.order.not.found";
    public static final String BAD_REQUEST = "error.message.bad.request";
    public static final String INTERNAL_SERVER_ERROR = "error.message.internal.server.error";
    public static final String ORDER_CREATING_ERROR = "error.message.order.creating.error";
    public static final String WRONG_PRICE = "error.message.order.wrong.price";
    public static final String WRONG_DURATION = "error.message.order.wrong.duration";
    public static final String WRONG_SIZE = "error.message.pagination.wrong.size";
    public static final String WRONG_PAGE = "error.message.pagination.wrong.page";
    public static final String USER_WITHOUT_ORDER = "error.message.user.without.order";
    public static final String NOT_VALID_TAG_NAME = "error.message.valid.tag.name";
    public static final String NOT_VALID_CERTIFICATE_NAME = "error.message.valid.certificate.name";
    public static final String NOT_VALID_CERTIFICATE_DESCRIPTION = "error.message.valid.certificate.description";
    public static final String NOT_VALID_CERTIFICATE_PRICE = "error.message.valid.certificate.price";
    public static final String NOT_VALID_CERTIFICATE_TAGS = "error.message.valid.certificate.tags";
    public static final String NOT_VALID_CERTIFICATE_DURATION = "error.message.valid.certificate.duration";
    public static final String NOT_VALID_USER_ID = "error.message.valid.order.user";
    public static final String NOT_VALID_CERTIFICATE = "error.message.valid.order.certificate";

    public static final String TOKEN_EXPIRED = "error.message.valid.token.expired";
    public static final String USER_ID_EXIST = "error.message.user.is.exist";

    private ExceptionMessage() {
    }
}
