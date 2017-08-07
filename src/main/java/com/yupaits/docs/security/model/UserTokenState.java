package com.yupaits.docs.security.model;

/**
 * Created by yupaits on 2017/8/7.
 */
public class UserTokenState {
    private String accessToken;
    private Long expiredIn;

    public UserTokenState() {
    }

    public UserTokenState(String accessToken, Long expiredIn) {
        this.accessToken = accessToken;
        this.expiredIn = expiredIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(Long expiredIn) {
        this.expiredIn = expiredIn;
    }
}
