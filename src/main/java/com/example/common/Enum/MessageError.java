package com.example.common.Enum;

import org.springframework.http.HttpStatus;

public enum MessageError {

    //region Product message
    PRODUCT_IS_EXIST("Product is exists", HttpStatus.FORBIDDEN),
    PRODUCT_IMAGE_NOT_FOUND("Not found product image", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
    //endregion

    //region Brand message
    BRAND_NOT_FOUND("Brand not found", HttpStatus.NOT_FOUND),
    //endregion

    //region Category message
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
    //endregion

    //region User message
    USER_NOT_FOUND_GET_ALL("Error while get all user: No user in database", HttpStatus.NOT_FOUND),
    USER_NOT_MATCH("Error while get user: No user match condition search", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("Not found user", HttpStatus.NOT_FOUND),
    USER_IS_EXIST("User account is exists", HttpStatus.FORBIDDEN),
    USER_IS_DISABLE("User is disable", HttpStatus.UNAUTHORIZED),
    //endregion

    //region ComfirmKey message
    CONFIRM_KEY_INVALID("Confirm key invalid", HttpStatus.UNAUTHORIZED),
    //endregion

    //region Session message
    SESSION_NOT_FOUND("Session not found", HttpStatus.UNAUTHORIZED),
    DEVICE_INFO_INVALID("Device info invalid", HttpStatus.UNAUTHORIZED),
    //endregion

    //region Order message
    ORDER_NOT_FOUND("Not found order", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND_PRODUCT("Not found product in cart", HttpStatus.NOT_FOUND),
    //endregion

    //region Cart message
    CART_CREATED_ERROR("Error while created cart", HttpStatus.INTERNAL_SERVER_ERROR),
    CART_NOT_FOUND("Not found cart", HttpStatus.NOT_FOUND);
    //endregion

    private String message;
    private HttpStatus httpStatus;

    private MessageError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
