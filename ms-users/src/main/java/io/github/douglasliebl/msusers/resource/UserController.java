package io.github.douglasliebl.msusers.resource;

import io.github.douglasliebl.msusers.configuration.security.anotations.CanBanAnUser;
import io.github.douglasliebl.msusers.configuration.security.anotations.CanListAllUsers;
import io.github.douglasliebl.msusers.configuration.security.anotations.CanReadMyUser;
import io.github.douglasliebl.msusers.configuration.security.anotations.CanWriteMyUser;
import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import io.github.douglasliebl.msusers.dto.UserPasswordUpdateDTO;
import io.github.douglasliebl.msusers.dto.UserResponseDTO;
import io.github.douglasliebl.msusers.dto.UserUpdateDTO;
import io.github.douglasliebl.msusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserInsertDTO request) {
        var response = service.save(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/{email}").buildAndExpand(response.getEmail()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @CanReadMyUser
    @GetMapping("/me")
    public ResponseEntity me(@AuthenticationPrincipal Jwt jwt) {
        var response = service.me(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CanWriteMyUser
    @PutMapping("/update-data")
    public ResponseEntity updateMyUser(@AuthenticationPrincipal Jwt jwt,
                                       @RequestBody UserUpdateDTO updateData) {
        var response = service.updateMyUser(jwt, updateData);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CanWriteMyUser
    @PutMapping("/update-password")
    public ResponseEntity updateMyPassword(@AuthenticationPrincipal Jwt jwt,
                                           @RequestBody UserPasswordUpdateDTO newPassword) {
        var response = service.updateMyPassword(jwt, newPassword);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CanListAllUsers
    @GetMapping
    public ResponseEntity listAllClients(Pageable pageRequest) {
        List<UserResponseDTO> result = service.find(pageRequest)
                .map(UserResponseDTO::of)
                .toList();
        PageImpl<UserResponseDTO> pagedResponse = new PageImpl<>(result, pageRequest, result.size());

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse);
    }

    @CanWriteMyUser
    @DeleteMapping("/delete")
    public ResponseEntity deleteMyUser(@AuthenticationPrincipal Jwt jwt) {
        var response = service.delete(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CanBanAnUser
    @DeleteMapping("/ban")
    public ResponseEntity banAnUser(@RequestParam String email) {
        service.banUser(email);
        return ResponseEntity.noContent().build();
    }

}
