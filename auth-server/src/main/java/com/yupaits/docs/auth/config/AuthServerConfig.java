package com.yupaits.docs.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JedisConnectionFactory connectionFactory;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServerConfig(UserDetailsService userDetailsService, JedisConnectionFactory connectionFactory, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.connectionFactory = connectionFactory;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("docs-manage")
                .secret("yupaits")
                .scopes("all")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token");
    }
}
