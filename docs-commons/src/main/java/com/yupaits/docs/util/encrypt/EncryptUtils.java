package com.yupaits.docs.util.encrypt;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.Arrays;

/**
 * 加密工具类
 * Created by ts495 on 2017/9/10.
 */
public class EncryptUtils {
    private static String algorithmName = "SHA-256";
    private static int iterations = 3;
    private static String visitCodeSalt = Arrays.toString(Base64.decode("ZG9jcy1tYW5hZ2U="));

    public static String encryptPassword(String password, String salt) {
        return new SimpleHash(algorithmName, password, salt, iterations).toHex();
    }

    public static String encryptVisitCode(String visitCode) {
        return new SimpleHash(algorithmName, visitCode, visitCodeSalt, iterations).toBase64();
    }
}
