package io.github.douglasliebl.msusers.model.repositories;

import io.github.douglasliebl.msusers.model.entity.Role;
import io.github.douglasliebl.msusers.model.entity.User;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("Should return true when a cpf is already registered.")
    public void returnTrueWhenCpfAlreadyExistsTest() {
        // given
        String cpf = getUser().getCpf();
        entityManager.persist(getUser());

        // when
        boolean exists = repository.existsByCpf(cpf);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when a cpf isn't registered.")
    public void returnFalseWhenCpfAlreadyExistsTest() {
        // given
        String cpf = getUser().getCpf();

        // when
        boolean exists = repository.existsByCpf(cpf);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return true when a email is already in use.")
    public void returnTrueWhenEmailAlreadyExistsTest() {
        // given
        String email = getUser().getEmail();
        entityManager.persist(getUser());

        // when
        boolean exists = repository.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when a email isn't in use.")
    public void returnFalseWhenEmailAlreadyExistsTest() {
        // given
        String email = getUser().getEmail();

        // when
        boolean exists = repository.existsByEmail(email);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should get a user by email.")
    public void findByEmailTest() {
        // given
        User user = getUser();
        entityManager.persist(user);

        // when
        Optional<User> savedUser = repository.findByEmail(getUser().getEmail());

        // then
        assertThat(savedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should delete a book")
    public void deleteBookTest() {
        // given
        User user = getUser();
        entityManager.persist(user);

        // when
        User foundUser = entityManager.find(User.class, user.getId());
        repository.delete(foundUser);

        // then
        User deletedBook = entityManager.find(User.class, foundUser.getId());
        AssertionsForClassTypes.assertThat(deletedBook).isNull();
    }


    private static User getUser() {
        return User.builder()
                .firstName("teste")
                .lastName("teste")
                .cpf("11100011100")
                .email("teste@email.com")
                .password("dkfjadlskjfdlkaf")
                .createdAt(OffsetDateTime.now())
                .role(Role.CLIENT)
                .build();
    }
}