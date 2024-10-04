package com.dev.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * A Feign configuration to add the incoming Bearer token to an outgoing feign client request.
 * Only annotate this class with "@Configuration" if you want this interceptor to apply globally to all your Feign clients.
 * Otherwise you risk exposing the auth token to a third party, or adding it unnecessarily to requests that don't need it.
 */
@Configuration
@Slf4j
public class BearerAuthFeignConfig {

    @Bean
    public RequestInterceptor bearerAuthRequestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.debug("llegue aca");
            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                log.debug(String.valueOf(jwt));
                requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
            } else {
                log.error("Unable to add Authoriation header to Feign requestTemplate");
            }
        };
    }
}
