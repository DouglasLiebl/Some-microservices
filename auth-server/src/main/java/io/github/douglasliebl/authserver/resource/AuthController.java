package io.github.douglasliebl.authserver.resource;

import io.github.douglasliebl.authserver.dto.LoginDTO;
import io.github.douglasliebl.authserver.dto.RefreshTokenDTO;
import io.github.douglasliebl.authserver.dto.UserDTO;
import io.github.douglasliebl.authserver.service.TokenService;
import io.github.douglasliebl.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @GetMapping("/get-roles")
    public ResponseEntity getRoles(@RequestHeader("Authorization") String token) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenService.getPermissions(token));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity generateNewAccessToken(@RequestBody RefreshTokenDTO token) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.generateNewToken(token));
    }

    @DeleteMapping("/revoke-token")
    public ResponseEntity revokeRefreshToken(@RequestBody RefreshTokenDTO token) {
        tokenService.revokeRefreshToken(token);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
