package io.github.douglasliebl.msusers.service.impl;

import io.github.douglasliebl.msusers.dto.*;
import io.github.douglasliebl.msusers.exception.ResourceNotFoundException;
import io.github.douglasliebl.msusers.model.entity.User;
import io.github.douglasliebl.msusers.model.repositories.UserRepository;
import io.github.douglasliebl.msusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO save(UserInsertDTO request) {
        uniquenessChecker(request.getCpf(), request.getEmail());

        return UserResponseDTO.of(repository
                .save(User.of(request, passwordEncoder)));
    }

    @Override
    public UserDTO me(Jwt jwt) {
        Optional<User> response = Optional.ofNullable(getUser(jwt));
        return UserDTO.of(response);
    }

    @Override
    public UserResponseDTO updateMyUser(Jwt jwt, UserUpdateDTO data) {
        var user = getUser(jwt);

        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setEmail(data.getEmail());

        uniquenessChecker(user.getCpf(), user.getEmail());

        return UserResponseDTO.of(repository.save(user));
    }

    @Override
    public String updateMyPassword(Jwt jwt, UserPasswordUpdateDTO data) {
        var user = getUser(jwt);

        user.setPassword(passwordEncoder
                .encode(data.getPassword()));
        repository.save(user);

        return "Password successfully updated.";
    }

    @Override
    public Page<User> find(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    public String delete(Jwt jwt) {
        var user = getUser(jwt);
        repository.delete(user);

        return "You account was successfully deleted.";
    }

    @Override
    public void banUser(String email) {
        repository.delete(repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found.")));
    }

    @Override
    public UserDTO getDetailsByEmail(String email) {
        return UserDTO.of(Optional.of(repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."))));
    }

    private User getUser(Jwt jwt) {
        String email = jwt.getClaims().get("sub").toString();
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private void uniquenessChecker(String cpf, String email) {
        if (repository.existsByCpf(cpf)) throw new DataIntegrityViolationException("CPF already registered.");
        if (repository.existsByEmail(email)) throw new DataIntegrityViolationException("Email already in use.");
    }

}
