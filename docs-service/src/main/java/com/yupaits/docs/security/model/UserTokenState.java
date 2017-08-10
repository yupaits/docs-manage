package com.yupaits.docs.security.model;

/**
 * Created by yupaits on 2017/8/7.
 */
public class UserTokenState {
    private String accessToken;
    private UserDto user;
    private Long expiredAt;

    public UserTokenState() {
    }

    public UserTokenState(String accessToken, UserDto user, Long expiredAt) {
        this.accessToken = accessToken;
        this.user = user;
        this.expiredAt = expiredAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "UserTokenState{" +
                "accessToken='" + accessToken + '\'' +
                ", user=" + user +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
