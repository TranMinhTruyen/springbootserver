package com.ggapp.common.utils;

public class Constant {
    public static final String DATE_TIME_FORMAT_PATTERN = "dd-MM-yyyy HH:mm:ss z";
    public static final String DATE_FORMAT_PATTERN = "dd-MM-yyyy";

    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    public static final String REGISTER_TYPE = "REGISTER_TYPE";
    public static final String IMAGE_FILE_PATH = "D:/GGappImage/";
    public static final String USER_FILE_PATH = "UsersImage/";
    public static final String BRAND_FILE_PATH = "BrandsImage/";
    public static final String PRODUCT_FILE_PATH = "ProductsImage/";

    //JWT
    public static final long EXPIRATIONTIME = 86400000;
    public static final long EXPIRATIONTIME_FOR_REMEMBER = 1000 * 86400000L;
    public static final String SECRET = "UnlimitedBladeWork";

    //ORDER STATUS
    public static final String NEW = "NEW";
    public static final String PROCESSING = "PROCESSING";
    public static final String DELIVERY = "DELIVERY";
    public static final String DONE = "DONE";

    //VOUCHER TYPE
    public static final String ALLTYPE = "ALLTYPE";
    public static final String SINGLETYPE = "SINGLETYPE";

    //DISCOUNT TYPE
    public static final String PERCENT = "PERCENT";
    public static final String FLAT = "FLAT";

    //DEVICE STATUS
    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";

    //ACCOUNT TYPE
    public static final String USER_TYPE = "USER";
    public static final String EMPLOYEE_TYPE = "EMPLOYEE";
    public static final String ADMIN_TYPE = "ADMIN";

    //AUTHORITY TYPE
    public static final String ALL_PERMISSION = "ALL";
    public static final String CREATED_PERMISSION = "CREATED";
    public static final String UPDATE_PERMISSION = "UPDATE";
    public static final String GET_PERMISSION= "GET";
    public static final String DELETED_PERMISSION = "DELETED";
}
