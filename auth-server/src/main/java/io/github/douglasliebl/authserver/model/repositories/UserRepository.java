package io.github.douglasliebl.authserver.model.repositories;

import io.github.douglasliebl.authserver.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
