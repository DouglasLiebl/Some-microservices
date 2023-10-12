package io.github.douglasliebl.authserver.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Getter @Setter
@ConfigurationProperties("io.auth")
public class AuthProperties {

    @NotBlank
    private String providerUri;

    @NotNull
    private JksProperties jks;

    @Getter @Setter
    static class JksProperties {

        @NotBlank
        private String keypass;

        @NotBlank
        private String storepass;

        @NotBlank
        private String alias;

        @NotBlank
        private String path;
    }
}
