package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Response;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.security.helper.TokenHelper;
import com.yupaits.docs.security.model.User;
import com.yupaits.docs.security.model.UserDTO;
import com.yupaits.docs.security.model.UserTokenState;
import com.yupaits.docs.security.service.DefaultUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yupaits on 2017/8/8.
 */
@PreAuthorize("hasRole('USER')")
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

    @Autowired
    private DefaultUserDetailsService defaultUserDetailsService;

    @GetMapping("/currentUser")
    public Response<UserDTO> getCurrentUser(HttpServletRequest request) {
        UserDTO currentUser = null;
        String authToken = tokenHelper.getToken(request);
        if (authToken != null) {
            User user = (User) defaultUserDetailsService.loadUserByUsername(tokenHelper.getUsernameFromToken(authToken));
            if (user != null) {
                currentUser = new UserDTO();
                BeanUtils.copyProperties(user, currentUser);
            }
        }
        return ResponseBuilder.ok(currentUser);
    }

    @GetMapping("/refresh")
    public Response<UserTokenState> refreshAuthToken(HttpServletRequest request) {
        String authToken = tokenHelper.getToken(request);
        UserTokenState userTokenState = null;
        if (authToken != null && tokenHelper.canRefreshToken(authToken)) {
            //刷新token
            String refreshedToken = tokenHelper.refreshToken(authToken);
            User user = (User) defaultUserDetailsService.loadUserByUsername(tokenHelper.getUsernameFromToken(authToken));
            UserDTO userDTO = null;
            if (user != null) {
                userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
            }
            userTokenState = new UserTokenState(refreshedToken, userDTO, tokenHelper.generateExpirationTimeMillis(expiredIn));
            logger.debug("refresh auth-token, username: {}", tokenHelper.getUsernameFromToken(authToken));
        }
        return ResponseBuilder.ok(userTokenState);
    }
}
