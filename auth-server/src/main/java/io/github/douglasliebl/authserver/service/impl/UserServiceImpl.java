package io.github.douglasliebl.authserver.service.impl;

import io.github.douglasliebl.authserver.dto.JwtResponseDTO;
import io.github.douglasliebl.authserver.dto.LoginDTO;
import io.github.douglasliebl.authserver.dto.RefreshTokenDTO;
import io.github.douglasliebl.authserver.dto.UserDTO;
import io.github.douglasliebl.authserver.model.entity.RefreshToken;
import io.github.douglasliebl.authserver.model.entity.User;
import io.github.douglasliebl.authserver.model.repositories.UserRepository;
import io.github.douglasliebl.authserver.service.TokenService;
import io.github.douglasliebl.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository repository;
    private final ApplicationContext context;
    private final TokenService tokenService;

    AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }

    @Override
    public UserDTO register(UserDTO request) {
        emailCheck(request.getEmail());
        cpfCheck(request.getCpf());

        return UserDTO.of(repository
                .save(User.of(request)));
    }

    @Override
    public JwtResponseDTO login(LoginDTO request) {
        authenticationManager = context.getBean(AuthenticationManager.class);

        var usernamePassword = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return JwtResponseDTO.builder()
                .accessToken(tokenService.generateToken((User) auth.getPrincipal()))
                .refreshToken(tokenService.generateRefreshToken(request.getUsername()).getRefreshToken())
                .build();
    }

    @Override
    public Optional<JwtResponseDTO> generateNewToken(RefreshTokenDTO request) {
        return tokenService.findByToken(request.getRefreshToken())
                .map(tokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                   String accessToken = tokenService.generateToken(user);
                   var newRefreshToken = tokenService.generateRefreshToken(user.getEmail());
                   return JwtResponseDTO.builder()
                           .accessToken(accessToken)
                           .refreshToken(newRefreshToken.getRefreshToken()).build();
                });
    }

    private void emailCheck(String email) {
        if (repository.existsByEmail(email)) throw new DataIntegrityViolationException("Email already in use.");
    }

    private void cpfCheck(String cpf) {
        if (repository.existsByCpf(cpf)) throw new DataIntegrityViolationException("CPF already registered.");
    }
}
