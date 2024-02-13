package io.github.douglasliebl.authserver.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {

    private String accessToken;
    private String refreshToken;
}
