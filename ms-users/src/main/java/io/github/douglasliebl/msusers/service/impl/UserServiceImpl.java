package io.github.douglasliebl.msusers.service.impl;

import io.github.douglasliebl.msusers.dto.UserDTO;
import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import io.github.douglasliebl.msusers.dto.UserResponseDTO;
import io.github.douglasliebl.msusers.model.entity.User;
import io.github.douglasliebl.msusers.model.repositories.UserRepository;
import io.github.douglasliebl.msusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO save(UserInsertDTO request) {
        uniquenessChecker(request);

        return UserResponseDTO.of(repository
                .save(User.of(request, passwordEncoder)));
    }

    @Override
    public UserDTO me(Jwt jwt) {
        String email = jwt.getClaims().get("sub").toString();

        return UserDTO.of(repository.findByEmail(email));
    }

    private void uniquenessChecker(UserInsertDTO request) {
        if (repository.existsByCpf(request.getCpf())) throw new DataIntegrityViolationException("CPF already registered.");
        if (repository.existsByEmail(request.getEmail())) throw new DataIntegrityViolationException("Email already in use.");
    }

}
