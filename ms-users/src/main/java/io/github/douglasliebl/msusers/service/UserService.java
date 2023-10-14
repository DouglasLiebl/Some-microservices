package io.github.douglasliebl.msusers.service;

import io.github.douglasliebl.msusers.dto.*;
import io.github.douglasliebl.msusers.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    UserResponseDTO save(UserInsertDTO request);

    UserDTO me(Jwt jwt);

    UserResponseDTO updateMyUser(Jwt jwt, UserUpdateDTO data);

    String updateMyPassword(Jwt jwt, UserPasswordUpdateDTO data);

    Page<User> find(Pageable pageRequest);

    String delete(Jwt jwt);

    void banUser(String email);

    UserDTO getDetailsByEmail(String email);
}
