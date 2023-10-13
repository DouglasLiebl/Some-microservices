package io.github.douglasliebl.msusers.dto;

import io.github.douglasliebl.msusers.model.entity.User;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Optional;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String cpf;
    private String email;
    private String password;
    private String role;
    private OffsetDateTime createdAt;

    public static UserDTO of(Optional<User> user) {
        return UserDTO.builder()
                .id(user.get().getId())
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .cpf(user.get().getCpf())
                .email(user.get().getEmail())
                .password(String.format("Encrypted password: %s", user.get().getPassword()))
                .role(user.get().getRole().name())
                .createdAt(user.get().getCreatedAt())
                .build();
    }

}
