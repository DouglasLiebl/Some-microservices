package io.github.douglasliebl.msusers.model.entity;

import io.github.douglasliebl.msusers.dto.UserDTO;
import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private OffsetDateTime createdAt;

    public static User of(UserDTO dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.valueOf(dto.getRole()))
                .createdAt(dto.getCreatedAt())
                .build();
    }

    public static User of(UserInsertDTO insertDTO, PasswordEncoder encoder) {
        return User.builder()
                .firstName(insertDTO.getFirstName())
                .lastName(insertDTO.getLastName())
                .cpf(insertDTO.getCpf())
                .email(insertDTO.getEmail())
                .password(encoder.encode(insertDTO.getPassword()))
                .role(Role.CLIENT)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
