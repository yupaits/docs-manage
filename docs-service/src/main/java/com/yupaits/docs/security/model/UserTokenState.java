package com.yupaits.docs.security.model;

/**
 * Created by yupaits on 2017/8/7.
 */
public class UserTokenState {
    private String accessToken;
    private String username;
    private Long expiredAt;

    public UserTokenState() {
    }

    public UserTokenState(String accessToken, String username, Long expiredAt) {
        this.accessToken = accessToken;
        this.username = username;
        this.expiredAt = expiredAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }
}
