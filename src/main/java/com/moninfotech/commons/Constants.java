package com.moninfotech.commons;

/**
 * Created by sayemkcn on 4/2/17.
 */
public class Constants {
    private Constants(){}

    public static String FIELD_ID = "id";

    public static int PAGE_SIZE = 10;

    public static final class Roles{
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_HOTEL_ADMIN = "ROLE_HOTEL_ADMIN";
        public static final String ROLE_AGENT = "ROLE_AGENT";


        // Authorities
        public static final String AUTHORITY_ADMIN = "ADMIN";
        public static final String AUTHORITY_USER = "USER";
        public static final String AUTHORITY_HOTEL_ADMIN = "HOTEL_ADMIN";
        public static final String AUTHORITY_AGENT = "AGENT";
    }

}
