package com.ggapp.common.enums;

import org.springframework.http.HttpStatus;

public enum MessageResponse {

    //region Request error message
    VERSION_IS_NOT_LATEST("The version is not latest please reload version", HttpStatus.BAD_REQUEST),
    //endregion

    //region Product message
    PRODUCT_IS_EXIST("Product is exists", HttpStatus.FORBIDDEN),
    PRODUCT_IMAGE_NOT_FOUND("Not found product image", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
    CREATE_PRODUCT_SUCCESSFUL("Create product successful", HttpStatus.OK),
    GET_PRODUCT_SUCCESSFUL("Get product successful", HttpStatus.OK),
    DELETE_PRODUCT_IMAGE_SUCCESSFUL("Delete image successful", HttpStatus.OK),
    DELETE_PRODUCT_SUCCESSFUL("Delete product successful", HttpStatus.OK),
    UPDATE_PRODUCT_SUCCESSFUL("Update product successful", HttpStatus.OK),
    //endregion

    //region Store message
    STORE_NOT_FOUND("Store not found", HttpStatus.NOT_FOUND),
    //endregion

    //region ProductStore message
    PRODUCT_IN_STORE_NOT_FOUND("Product in store not found", HttpStatus.NOT_FOUND),
    PRODUCT_IS_OUT_OF_STOCK("Product is out of stock", HttpStatus.NOT_FOUND),
    //endregion

    //region Brand message
    BRAND_NOT_FOUND("Brand not found", HttpStatus.NOT_FOUND),
    BRAND_IS_EXIST("Brand is exists", HttpStatus.FORBIDDEN),
    //endregion

    //region Email message
    EMAIL_SEND_SUCCESS("Email has been sent", HttpStatus.OK),
    //endregion

    //region Category message
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_IS_EXIST("Category is exists", HttpStatus.FORBIDDEN),
    CREATED_CATEGORY_SUCCESSFUL("Created category successful", HttpStatus.OK),
    UPDATE_CATEGORY_SUCCESSFUL("Updated category successful", HttpStatus.OK),
    LOGIC_DELETED_CATEGORY_SUCCESSFUL("Logic deleted category successful", HttpStatus.OK),
    PHYSIC_DELETED_CATEGORY_SUCCESSFUL("Physic deleted category successful", HttpStatus.OK),
    GET_CATEGORY_SUCCESSFUL("Get category successful", HttpStatus.OK),
    //endregion

    //region Account message
    ACCESS_DENIED("Access denied", HttpStatus.UNAUTHORIZED),
    LOGIN_VALID("Login successful", HttpStatus.OK),
    LOGOUT_SUCCESS("Logout successful", HttpStatus.OK),
    //endregion

    //region User message
    USER_NOT_FOUND_GET_ALL("Error while get all user: No user in database", HttpStatus.NOT_FOUND),
    USER_NOT_MATCH("Error while get user: No user match condition search", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("Not found user", HttpStatus.NOT_FOUND),
    USER_IS_EXIST("User account is exists", HttpStatus.FORBIDDEN),
    USER_IS_DISABLE("User is disable", HttpStatus.UNAUTHORIZED),
    USER_CREATED_SUCCESS("User is added", HttpStatus.OK),
    GET_USER_SUCCESS("Get user success", HttpStatus.OK),
    GET_PROFILE_USER_SUCCESS("Get profile user success", HttpStatus.OK),
    UPDATE_USER_SUCCESS("User update success", HttpStatus.OK),
    DELETED_USER_SUCCESS("User delete success", HttpStatus.OK),
    LOGIC_DELETED_USER_SUCCESS("User logic delete success", HttpStatus.OK),
    //endregion

    //region Employee message
    EMPLOYEE_NOT_MATCH("Error while get employee: No employee match condition search", HttpStatus.NOT_FOUND),
    EMPLOYEE_NOT_FOUND("Not found employee", HttpStatus.NOT_FOUND),
    EMPLOYEE_CREATED_SUCCESS("Employee is added", HttpStatus.OK),
    EMPLOYEE_IS_EXIST("Employee account is exists", HttpStatus.FORBIDDEN),
    //endregion

    //region ComfirmKey message
    CONFIRM_KEY_INVALID("Confirm key invalid", HttpStatus.UNAUTHORIZED),
    CONFIRM_KEY_EXPIRED("Confirm key expired", HttpStatus.UNAUTHORIZED),
    //endregion

    //region Session message
    SESSION_NOT_FOUND("Session not found", HttpStatus.UNAUTHORIZED),
    DEVICE_INFO_INVALID("Device info invalid", HttpStatus.UNAUTHORIZED),
    DEVICE_ALREADY_LOGIN("Device already login", HttpStatus.UNAUTHORIZED),
    //endregion

    //region Order message
    ORDER_NOT_FOUND("Not found order", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND_PRODUCT("Not found product in cart", HttpStatus.NOT_FOUND),
    CREATED_ORDER_SUCCESSFUL("Order created successful", HttpStatus.OK),
    GET_ORDER_SUCCESSFUL("Get order successful", HttpStatus.OK),
    UPDATED_ORDER_SUCCESSFUL("Order updated successful", HttpStatus.OK),
    DELETED_ORDER_SUCCESSFUL("Order deleted successful", HttpStatus.OK),
    //endregion

    //region Cart message
    CART_CREATED_ERROR("Error while created cart", HttpStatus.INTERNAL_SERVER_ERROR),
    CREATED_CART_SUCCESSFUL("Created cart successful", HttpStatus.OK),
    UPDATED_CART_SUCCESSFUL("Updated cart successful", HttpStatus.OK),
    GET_CART_SUCCESSFUL("Get cart successful", HttpStatus.OK),
    DELETED_CART_SUCCESSFUL("Deleted cart successful", HttpStatus.OK),
    REMOVED_PRODUCT_FROM_CART_SUCCESSFUL("Product is removed successful", HttpStatus.OK),
    UPDATED_PRODUCT_AMOUNT_SUCCESSFUL("Product amount updated successful", HttpStatus.OK),
    CART_NOT_FOUND("Not found cart", HttpStatus.NOT_FOUND);
    //endregion

    private String message;
    private HttpStatus httpStatus;

    MessageResponse(String message, HttpStatus httpStatus) {
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
