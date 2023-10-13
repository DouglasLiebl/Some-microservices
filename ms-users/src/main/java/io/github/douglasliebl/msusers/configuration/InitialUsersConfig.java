package io.github.douglasliebl.msusers.configuration;

import io.github.douglasliebl.msusers.model.entity.Role;
import io.github.douglasliebl.msusers.model.entity.User;
import io.github.douglasliebl.msusers.model.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class InitialUsersConfig implements ApplicationRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.count() != 0) return;

        User admin = User.builder()
                .firstName("ADMIN")
                .lastName("ADMIN")
                .cpf("0001110011")
                .email("admin@email.com")
                .password(passwordEncoder.encode("1234567"))
                .role(Role.ADMIN)
                .createdAt(OffsetDateTime.now())
                .build();

        User client = User.builder()
                .firstName("CLIENT")
                .lastName("CLIENT")
                .cpf("11100011100")
                .email("client@email.com")
                .password(passwordEncoder.encode("1234567"))
                .role(Role.CLIENT)
                .createdAt(OffsetDateTime.now())
                .build();

        repository.save(admin);
        repository.save(client);
    }
}
