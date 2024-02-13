package io.github.douglasliebl.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private CheckPermissions checkPermissions;

    public AuthenticationFilter() {
        super(AuthenticationFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return checkPermissions.getRoles("ROLE_USER");
    }

    public static class Config {

    }
}
