package com.moninfotech.commons;


import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by sayemkcn on 2/5/17.
 */
public final class SessionIdentifierGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    public String nextPassword(){
        SecureRandom random = new SecureRandom();
        return "p"+(10000+random.nextInt(9000000));
    }
}