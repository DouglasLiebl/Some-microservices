package io.github.douglasliebl.authserver.service;

import io.github.douglasliebl.authserver.dto.JwtResponseDTO;
import io.github.douglasliebl.authserver.dto.LoginDTO;
import io.github.douglasliebl.authserver.dto.RefreshTokenDTO;
import io.github.douglasliebl.authserver.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    UserDTO register(UserDTO request);
    JwtResponseDTO login(LoginDTO request);
    Optional<JwtResponseDTO> generateNewToken(RefreshTokenDTO request);
}
