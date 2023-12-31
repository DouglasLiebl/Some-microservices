package io.github.douglasliebl.msusers.model.repositories;

import io.github.douglasliebl.msusers.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

}
