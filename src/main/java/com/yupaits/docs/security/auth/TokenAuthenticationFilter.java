package com.yupaits.docs.security.auth;

import com.yupaits.docs.security.helper.TokenHelper;
import com.yupaits.docs.security.model.User;
import com.yupaits.docs.security.service.DefaultUserDetailsService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yupaits on 2017/8/7.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private DefaultUserDetailsService defaultUserDetailsService;

    public static final String ROOT_MATCHER = "/";
    public static final String FAVICON_MATCHER = "/favicon.ico";
    public static final String HTML_MATCHER = "/**/*.html";
    public static final String CSS_MATCHER = "/**/*.css";
    public static final String JS_MATCHER = "/**/*.js";
    public static final String IMG_MATCHER = "/images/*";
    public static final String LOGIN_MATCHER = "/auth/login";
    public static final String LOGOUT_MATCHER = "/auth/logout";

    private List<String> pathsToSkip = Arrays.asList(
            ROOT_MATCHER,
            HTML_MATCHER,
            FAVICON_MATCHER,
            CSS_MATCHER,
            JS_MATCHER,
            IMG_MATCHER,
            LOGIN_MATCHER,
            LOGOUT_MATCHER
    );

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authToken = tokenHelper.getToken(httpServletRequest);
        if (authToken != null && !skipPathRequest(httpServletRequest, pathsToSkip)) {
            try {
                String username = tokenHelper.getUsernameFromToken(authToken);
                User user = (User) defaultUserDetailsService.loadUserByUsername(username);
                TokenBasedAuthentication authentication = new TokenBasedAuthentication(user);
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e) {
                SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthentication());
            }
        }
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthentication());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean skipPathRequest(HttpServletRequest request, List<String> pathsToSkip) {
        Assert.notNull(pathsToSkip, "pathsToSkip is null!");
        List<RequestMatcher> matcherList = new ArrayList<>();
        RequestMatcher requestMatcher = null;
        for (String path : pathsToSkip) {
            requestMatcher = new AntPathRequestMatcher(path);
            matcherList.add(requestMatcher);
        }
        OrRequestMatcher orRequestMatcher = new OrRequestMatcher(matcherList);
        return orRequestMatcher.matches(request);
    }
}
