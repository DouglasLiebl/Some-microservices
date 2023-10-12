package io.github.douglasliebl.msusers.dto;

import lombok.*;

import java.time.OffsetDateTime;

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


}
