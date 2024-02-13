package io.github.douglasliebl.authserver.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {

    private String refreshToken;
}
