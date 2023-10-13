package io.github.douglasliebl.msusers.resource;

import io.github.douglasliebl.msusers.configuration.security.anotations.CanReadUser;
import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import io.github.douglasliebl.msusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserInsertDTO request) {
        var response = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @CanReadUser
    @GetMapping
    public ResponseEntity me(@AuthenticationPrincipal Jwt jwt) {
        var response = service.me(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
