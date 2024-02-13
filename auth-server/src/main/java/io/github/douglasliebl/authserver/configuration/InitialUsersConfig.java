package io.github.douglasliebl.authserver.configuration;

import io.github.douglasliebl.authserver.model.entity.Role;
import io.github.douglasliebl.authserver.model.entity.User;
import io.github.douglasliebl.authserver.model.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

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
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();

        User client = User.builder()
                .firstName("CLIENT")
                .lastName("CLIENT")
                .cpf("11100011100")
                .email("client@email.com")
                .password(passwordEncoder.encode("user"))
                .role(Role.USER)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();

        repository.save(admin);
        repository.save(client);
    }
}
