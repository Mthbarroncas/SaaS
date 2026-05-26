package com.fraudshield.platform.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class MerchantApiKeyFilter extends OncePerRequestFilter {

    private final String merchantApiKey;
    private final String dashboardBearerToken;

    public MerchantApiKeyFilter(
            @Value("${fraudshield.security.merchant-api-key}") String merchantApiKey,
            @Value("${fraudshield.security.dashboard-bearer-token}") String dashboardBearerToken) {
        this.merchantApiKey = merchantApiKey;
        this.dashboardBearerToken = dashboardBearerToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/actuator") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("X-API-Key");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (merchantApiKey.equals(apiKey) || ("Bearer " + dashboardBearerToken).equals(authorization)) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    "fraudshield-client",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_API")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
