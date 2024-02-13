package io.github.douglasliebl.authserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglasliebl.authserver.model.entity.Role;
import io.github.douglasliebl.authserver.model.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenService {

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

    public Object getPermissions(String token) throws JsonProcessingException {
        String validate = validateToken(token);
        if (validate.isEmpty()) return validate;

        ObjectMapper objectMapper = new ObjectMapper();
        String roles = JWT.decode(token).getClaim("roles").toString();

        JsonNode jsonNode = objectMapper.readTree(roles);
        String role = jsonNode.get("roles").get(0).toString().replace("\"", "");

        if (Objects.equals(role, Role.ADMIN.getRole())) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
