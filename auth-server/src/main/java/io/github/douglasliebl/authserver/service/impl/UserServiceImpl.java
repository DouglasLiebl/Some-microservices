package io.github.douglasliebl.authserver.service.impl;

import io.github.douglasliebl.authserver.dto.UserDTO;
import io.github.douglasliebl.authserver.model.entity.User;
import io.github.douglasliebl.authserver.model.repositories.UserRepository;
import io.github.douglasliebl.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserDTO register(UserDTO request) {
        emailCheck(request.getEmail());
        cpfCheck(request.getCpf());

        return UserDTO.of(repository
                .save(User.of(request)));
    }

    private void emailCheck(String email) {
        if (repository.existsByEmail(email)) throw new DataIntegrityViolationException("Email already in use.");
    }

    private void cpfCheck(String cpf) {
        if (repository.existsByCpf(cpf)) throw new DataIntegrityViolationException("CPF already registered.");
    }
}
