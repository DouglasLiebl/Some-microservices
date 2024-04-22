package io.github.douglasliebl.authserver.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglasliebl.authserver.dto.RefreshTokenDTO;
import io.github.douglasliebl.authserver.exception.ResourceNotFoundException;
import io.github.douglasliebl.authserver.model.entity.RefreshToken;
import io.github.douglasliebl.authserver.model.entity.Role;
import io.github.douglasliebl.authserver.model.entity.User;
import io.github.douglasliebl.authserver.model.repositories.RefreshTokenRepository;
import io.github.douglasliebl.authserver.model.repositories.UserRepository;
import io.github.douglasliebl.authserver.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public String generateToken(User user) {

        Map<String, ArrayList<String>> claims = new HashMap<>();

        ArrayList<String> roles = new ArrayList<>();
        roles.add(user.getRole().getRole());

        claims.put("roles", roles);

        try {
            return JWT.create()
                    .withIssuer("auth-server")
                    .withSubject(user.getEmail())
                    .withClaim("roles", claims)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                    .sign(Algorithm.HMAC256("c78f53c9c6676583f2ecdc38c2a512b97add5f8fb21e4a079d107374f1170f93"));

        } catch (JWTCreationException e) {
            throw new RuntimeException("ERROR WHILE GENERATING TOKEN", e);
        }
    }

    @Override
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("c78f53c9c6676583f2ecdc38c2a512b97add5f8fb21e4a079d107374f1170f93");

            return JWT.require(algorithm)
                    .withIssuer("auth-server")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            return "";
        }
    }

    @Override
    public RefreshToken generateRefreshToken(String email) {
        User user = userRepository.findByEmail(email);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();

        try {
            RefreshTokenDTO dto = RefreshTokenDTO.builder()
                    .refreshToken(refreshTokenRepository.findByUserId(user.getId()).getRefreshToken()).build();
            revokeRefreshToken(dto);
            return refreshTokenRepository
                    .save(refreshToken);
        } catch (Exception e) {
            throw new DataIntegrityViolationException("Same user cannot have two Refresh Tokens. Please revoke or generate a new access token from current Refresh Token.");
        }
    }

    @Override
    public Optional<RefreshToken> findByToken(String refreshToken) {
        var response = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (response.isEmpty()) throw new ResourceNotFoundException("Refresh Token not found. Please do Login again.");
        return response;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Token expired. Please do Login again.");
        }
        refreshTokenRepository.delete(refreshToken);
        return refreshToken;
    }

    @Override
    public void revokeRefreshToken(RefreshTokenDTO refreshToken) {
        var token = refreshTokenRepository.findByRefreshToken(refreshToken.getRefreshToken());
        if (token.isEmpty()) throw new ResourceNotFoundException("Refresh Token not found. Please do Login again.");
        RefreshToken refreshToken1 = RefreshToken.builder()
                .id(token.get().getId())
                .refreshToken((token.get().getRefreshToken()))
                .expiryDate(token.get().getExpiryDate()).build();

        refreshTokenRepository.delete(refreshToken1);
    }

    @Override
    public Object getPermissions(String token) throws JsonProcessingException {
        String cleanToken = token.replace("Bearer", "").replaceAll("\"", "");

        String validate = validateToken(cleanToken);
        if (validate.isEmpty()) return validate;

        ObjectMapper objectMapper = new ObjectMapper();
        String roles = JWT.decode(cleanToken).getClaim("roles").toString();

        JsonNode jsonNode = objectMapper.readTree(roles);
        String role = jsonNode.get("roles").get(0).toString().replace("\"", "");

        if (Objects.equals(role, Role.ADMIN.getRole())) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
