package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Response;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.security.helper.TokenHelper;
import com.yupaits.docs.security.model.UserTokenState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yupaits on 2017/8/8.
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.expiredIn}")
    private int expiredIn;

    @Value("${jwt.cookie}")
    private String tokenCookie;

    @Autowired
    private TokenHelper tokenHelper;

    @GetMapping("/refresh")
    public Response<UserTokenState> refreshAuthToken(HttpServletRequest request, HttpServletResponse response) {
        String authToken = tokenHelper.getToken(request);
        UserTokenState userTokenState = null;
        if (authToken != null && tokenHelper.canRefreshToken(authToken)) {
            //刷新token
            String refreshedToken = tokenHelper.refreshToken(authToken);
            userTokenState = new UserTokenState(refreshedToken, tokenHelper.getUsernameFromToken(authToken), tokenHelper.generateExpirationTimeMillis(expiredIn));
            logger.info("refresh auth-token, username: {}", tokenHelper.getUsernameFromToken(authToken));
        }
        return ResponseBuilder.ok(userTokenState);
    }
}
