package com.yupaits.docs.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@EnableOAuth2Sso
@SpringBootApplication
public class DocsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocsClientApplication.class, args);
    }
}
