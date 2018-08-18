package com.yupaits.docs.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.yupaits.docs.common.constants.DocsConsts;
import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.config.JwtHelper;
import com.yupaits.docs.dto.TokenRefresh;
import com.yupaits.docs.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/18
 */
public class TokenAuthFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtHelper jwtHelper;
    private final RedisTemplate redisTemplate;
    private final UserDetailsService userDetailsService;

    public TokenAuthFilter(ObjectMapper objectMapper, JwtHelper jwtHelper, RedisTemplate redisTemplate,
                           UserDetailsService userDetailsService) {
        this.objectMapper = objectMapper;
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtHelper.getToken(request);
        if (StringUtils.isBlank(token)) {
            if (skipPathRequest(request)) {
                //请求路径无需鉴权时，token为空也可访问
                SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthentication());
            } else {
                //请求路径需要鉴权，如果token为空则返回UNAUTHORIZED
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.UNAUTHORIZED));
            }
            filterChain.doFilter(request, response);
        } else {
            //判断token是否过期以及是否能被解析
            boolean tokenExpired = false;
            Claims claims = null;
            try {
                claims = jwtHelper.getClaimsFromToken(token);
            } catch (ExpiredJwtException | SignatureException e) {
                tokenExpired = true;
            }
            if (tokenExpired) {
                //当前token已过期并且尚可刷新，刷新token并放行；当前token无可刷新记录或已过可刷新时间，需要重新登录
                TokenRefresh tokenRefresh = (TokenRefresh) redisTemplate.opsForValue().get(DocsConsts.REFRESH_TTL_KEY + token.hashCode());
                if (tokenRefresh != null && new Date().compareTo(tokenRefresh.getRefreshDeadline()) < 0) {
                    String refreshedToken = jwtHelper.generateToken(tokenRefresh.getUsername());
                    redisTemplate.delete(DocsConsts.REFRESH_TTL_KEY + token.hashCode());
                    token = refreshedToken;
                    response.setHeader(DocsConsts.AUTH_HEADER_NAME, refreshedToken);
                } else {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.TOKEN_REFRESH_TIMEOUT));
                }
            } else if (claims == null) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.TOKEN_ILLEGAL));
            }
            //从请求中获取token中的username
            String username = jwtHelper.getUsernameFromToken(token);
            if (StringUtils.isBlank(username)) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.TOKEN_INVALID));
            }
            //将当前username缓存中的有效token与当前token进行匹配，匹配失败则token无效
            if (!StringUtils.equals((CharSequence) redisTemplate.opsForHash().get(DocsConsts.VALID_TOKEN_STORE, username), token)) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.TOKEN_INVALID));
            }
            try {
                User user = (User) userDetailsService.loadUserByUsername(username);
                TokenAuthentication authentication = new TokenAuthentication(user);
                authentication.setToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (UsernameNotFoundException e) {
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.UNAUTHORIZED));
            }
        }
    }

    private boolean skipPathRequest(HttpServletRequest request) {
        List<RequestMatcher> matchers = Lists.newArrayList();
        Arrays.stream(DocsConsts.ignorePaths).forEach(path -> matchers.add(new AntPathRequestMatcher(path)));
        return new OrRequestMatcher(matchers).matches(request);
    }
}
