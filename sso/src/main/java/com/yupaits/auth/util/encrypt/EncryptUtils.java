package com.yupaits.auth.util.encrypt;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 加密工具类
 * Created by ts495 on 2017/9/10.
 */
public class EncryptUtils {
    private static String algorithmName = "SHA-256";
    private static int iterations = 3;

    public static String encryptPassword(String password, String salt) {
        return new SimpleHash(algorithmName, password, salt, iterations).toHex();
    }
}
