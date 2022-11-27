package com.ggapp.common.utils;

public class Constant {
    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    public static final String REGISTER_TYPE = "REGISTER_TYPE";
    public static final String IMAGE_FILE_PATH = "D:/GGappImage/";

    //JWT
    public static final long EXPIRATIONTIME = 86400000;
    public static final long EXPIRATIONTIME_FOR_REMEMBER = 1000 * 2592000L;
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
}
