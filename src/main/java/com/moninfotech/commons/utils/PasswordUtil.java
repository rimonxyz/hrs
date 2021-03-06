package com.moninfotech.commons.utils;

import com.moninfotech.exceptions.nullexceptions.NullPasswordException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    public static int PASS_MIN_SIZE = 6;
    public static final int CRYPTO_STRENGTH = 512;

    public enum EncType {SHA_ENCODER, BCRYPT_ENCODER}

    public static ShaPasswordEncoder getShaPasswordEncoder() {
        return new ShaPasswordEncoder(CRYPTO_STRENGTH);
    }

    public static BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String encryptPassword(String password, EncType encryptType, String salt) throws Exception, NullPasswordException {
        if (password == null) throw new NullPasswordException("Password can not be empty!");
        if (encryptType.equals(EncType.SHA_ENCODER))
            return getShaPasswordEncoder().encodePassword(password, salt);
        else if (encryptType.equals(EncType.BCRYPT_ENCODER))
            return getBCryptPasswordEncoder().encode(password);
        return getBCryptPasswordEncoder().encode(password);
    }

}
