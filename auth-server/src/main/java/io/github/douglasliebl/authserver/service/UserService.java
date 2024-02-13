package io.github.douglasliebl.authserver.service;

import io.github.douglasliebl.authserver.dto.UserDTO;

public interface UserService {

    UserDTO register(UserDTO request);
}
