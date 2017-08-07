package com.yupaits.docs;

import com.yupaits.docs.common.constant.ApplicationConstant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yupaits on 2017/8/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private ShaPasswordEncoder shaPasswordEncoder;

    @Test
    public void testPasswordEncoder() {
        Assert.assertEquals(shaPasswordEncoder.encodePassword("123456", "admin" + ApplicationConstant.ENCRYPT_EXTRA_SALT), "f71de501f0ff4ad3b7a8584a846cd911085424c6ecd09b588164c35152b4e264");

    }
}
