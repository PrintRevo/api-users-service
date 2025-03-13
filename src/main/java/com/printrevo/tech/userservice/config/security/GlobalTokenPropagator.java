package com.printrevo.tech.userservice.config.security;

import com.printrevo.tech.commonsecurity.clients.interceptors.AuthTokenPropagatorInterceptor;
import com.printrevo.tech.commonsecurity.security.providers.resolver.IAuthTokenProviderResolver;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class GlobalTokenPropagator extends UsersServiceFeignInterceptor {

    private final AuthTokenPropagatorInterceptor authTokenPropagatorInterceptor;

    public GlobalTokenPropagator(IAuthTokenProviderResolver accessTokenProvider) {
        authTokenPropagatorInterceptor = new AuthTokenPropagatorInterceptor(accessTokenProvider);
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        authTokenPropagatorInterceptor.apply(requestTemplate);
    }
}
