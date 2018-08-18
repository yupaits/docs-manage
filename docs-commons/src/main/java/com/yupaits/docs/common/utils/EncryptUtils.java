package com.yupaits.docs.common.utils;


import com.yupaits.docs.common.constants.EncryptConsts;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * @author yupaits
 * @date 2018/8/11
 */
public class EncryptUtils {

    private static ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(EncryptConsts.SHA_NUMBER);

    static {
        shaPasswordEncoder.setIterations(EncryptConsts.PASSWORD_ENCODER_ITERATIONS);
    }

    public static String encryptPassword(String password, String salt) {
         return shaPasswordEncoder.encodePassword(password, salt);
    }

}
