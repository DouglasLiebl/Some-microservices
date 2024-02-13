package io.github.douglasliebl.authserver.dto;

import io.github.douglasliebl.authserver.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private String password;
    private String role;
    private String createdAt;
    private String updatedAt;

    public static UserDTO of(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .cpf(entity.getCpf())
                .password(entity.getPassword())
                .role(entity.getRole().name())
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .build();
    }

}
