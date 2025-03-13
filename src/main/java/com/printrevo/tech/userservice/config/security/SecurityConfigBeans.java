package com.printrevo.tech.userservice.config.security;

import com.printrevo.tech.commonsecurity.config.SecurityProperties;
import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.commonsecurity.helpers.Cache;
import com.printrevo.tech.commonsecurity.helpers.oauth2.OauthChecker;
import com.printrevo.tech.commonsecurity.helpers.oauth2.TokenRefresher;
import com.printrevo.tech.commonsecurity.security.providers.ext.ClientAuthTokenProvider;
import com.printrevo.tech.commonsecurity.security.providers.ext.custom.DefaultOauthTokenProvider;
import com.printrevo.tech.commonsecurity.security.resolvers.DelegatingAuthenticationManagerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
public class SecurityConfigBeans {


    @Bean
    public RedisTemplate<String, AccessToken> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, AccessToken> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }

    @Bean
    Cache<String, AccessToken> accessTokenCache(RedisTemplate<String, AccessToken> accessTokenRedisTemplate) {
        return new CacheProvider(accessTokenRedisTemplate);
    }

    @Bean
    OauthChecker getKeycloakAuthChecker(TokenRefresher tokenRefresher, SecurityProperties securityProperties) {
        return new OauthChecker(tokenRefresher, securityProperties);
    }

    @Bean
    public ClientAuthTokenProvider clientAuthTokenProvider(Cache<String, AccessToken> accessTokenCache
            , SecurityProperties securityProperties) {
        return new DefaultOauthTokenProvider(accessTokenCache, securityProperties);
    }

    @Bean
    DelegatingAuthenticationManagerResolver delegatingAuthenticationManagerResolver(SecurityProperties securityProperties) {
        return new DelegatingAuthenticationManagerResolver(securityProperties);
    }


    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}
