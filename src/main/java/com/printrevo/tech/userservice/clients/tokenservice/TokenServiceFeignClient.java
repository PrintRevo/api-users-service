package com.printrevo.tech.userservice.clients.tokenservice;

import com.printrevo.tech.commonsecurity.security.providers.resolver.IAuthTokenProviderResolver;
import com.printrevo.tech.userservice.config.security.GlobalTokenPropagator;
import com.printrevo.tech.userservice.data.dto.goservices.GoApiResult;
import com.printrevo.tech.userservice.data.dto.tokenservice.EmailVerificationRequest;
import com.printrevo.tech.userservice.data.dto.tokenservice.VerificationRequestResponse;
import com.printrevo.tech.userservice.data.dto.tokenservice.VerificationValidationRequest;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "token-service", url = "${uris.root.token-service}"
        , configuration = TokenServiceFeignClient.Configuration.class)
public interface TokenServiceFeignClient {
    @PostMapping("v1/tokens/send-by-email")
    GoApiResult<VerificationRequestResponse> sendEmailVerificationRequest(
            @RequestBody EmailVerificationRequest emailVerificationRequest);

    @PostMapping("v1/tokens/verification")
    GoApiResult<VerificationRequestResponse> validateVerificationRequest(
            @RequestBody VerificationValidationRequest verificationValidationRequest);

    class Configuration {
        @Bean
        RequestInterceptor requestInterceptor(IAuthTokenProviderResolver tokenProviderResolver) {
            return new GlobalTokenPropagator(tokenProviderResolver);
        }
    }
}
