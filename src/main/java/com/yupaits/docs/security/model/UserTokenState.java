package com.yupaits.docs.security.model;

/**
 * Created by yupaits on 2017/8/7.
 */
public class UserTokenState {
    private String accessToken;
    private Long expiredAt;

    public UserTokenState() {
    }

    public UserTokenState(String accessToken, Long expiredAt) {
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }
}
