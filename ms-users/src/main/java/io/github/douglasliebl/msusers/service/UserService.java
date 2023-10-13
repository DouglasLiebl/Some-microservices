package io.github.douglasliebl.msusers.service;

import io.github.douglasliebl.msusers.dto.UserDTO;
import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import io.github.douglasliebl.msusers.dto.UserResponseDTO;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    UserResponseDTO save(UserInsertDTO request);

    UserDTO me(Jwt jwt);
}
