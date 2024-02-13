package io.github.douglasliebl.authserver.service;

import io.github.douglasliebl.authserver.dto.RefreshTokenDTO;
import io.github.douglasliebl.authserver.model.entity.RefreshToken;
import io.github.douglasliebl.authserver.model.entity.User;

import java.util.Optional;

public interface TokenService {

    String generateToken(User user);
    String validateToken(String token);
    RefreshToken generateRefreshToken(String email);
    Optional<RefreshToken> findByToken(String refreshToken);
    RefreshToken verifyExpiration(RefreshToken refreshToken);
    void revokeRefreshToken(RefreshTokenDTO refreshToken);
    Object getPermissions(String token) throws Exception;
}
