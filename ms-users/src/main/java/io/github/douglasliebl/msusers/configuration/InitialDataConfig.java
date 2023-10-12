package io.github.douglasliebl.msusers.configuration;

import io.github.douglasliebl.msusers.model.entity.Role;
import io.github.douglasliebl.msusers.model.entity.User;
import io.github.douglasliebl.msusers.model.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataConfig implements ApplicationRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.count() != 0) return;

        User admin = User.builder()
                .firstName("ADMIN")
                .lastName("ADMIN")
                .cpf("01010101011")
                .email("admin@email.com")
                .password(passwordEncoder.encode("1234567"))
                .role(Role.ADMIN).build();

        User client = User.builder()
                .firstName("CLIENT")
                .lastName("CLIENT")
                .cpf("11111111111")
                .email("client@email.com")
                .password(passwordEncoder.encode("1234567"))
                .role(Role.CLIENT).build();

        repository.save(admin);
        repository.save(client);
    }


}
