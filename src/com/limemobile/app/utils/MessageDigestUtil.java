
package com.limemobile.app.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

public class MessageDigestUtil {
    public static MessageDigestUtil getInstance() {
        if (instance == null)
            instance = new MessageDigestUtil();
        return instance;
    }

    private static MessageDigestUtil instance;

    private MessageDigest hash;

    public MessageDigestUtil() {
        try {
            hash = MessageDigest.getInstance("SHA-1");

        } catch (final NoSuchAlgorithmException e) {
            try {
                hash = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException e2) {
                final RuntimeException re = new RuntimeException("No available hashing algorithm");
                re.initCause(e2);
                throw re;
            }
        }
    }

    public String hash(String key) {
    	if (TextUtils.isEmpty(key))
    		return "";
        final byte[] ba;
        synchronized (hash) {
            hash.update(key.toString().getBytes());
            ba = hash.digest();
        }
        final BigInteger bi = new BigInteger(1, ba);
        final String result = bi.toString(16);
        if (result.length() % 2 != 0) {
            return "0" + result;
        }
        return result;
    }
}
