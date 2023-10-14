package io.github.douglasliebl.msusers.service;

import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import io.github.douglasliebl.msusers.dto.UserResponseDTO;
import io.github.douglasliebl.msusers.dto.UserUpdateDTO;
import io.github.douglasliebl.msusers.model.entity.Role;
import io.github.douglasliebl.msusers.model.entity.User;
import io.github.douglasliebl.msusers.model.repositories.UserRepository;
import io.github.douglasliebl.msusers.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    UserService service;

    @MockBean
    UserRepository repository;

    @MockBean
    PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        this.service = new UserServiceImpl(repository, encoder);
    }

    @Test
    @DisplayName("Should successfully save a user.")
    public void mustSaveAUserTest() {
        // given
        UserInsertDTO request = getUserInsertDTO();
        User savedUser = getUser();

        // when
        Mockito.when(repository.existsByCpf(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(repository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);

        Mockito.when(repository.save(Mockito.any(User.class)))
                .thenReturn(savedUser);

        // then
        UserResponseDTO user =  service.save(request);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(user.getFirstName()).isEqualTo(savedUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(savedUser.getLastName());
        assertThat(user.getRole()).isEqualTo(savedUser.getRole().name());
    }

    @Test
    @DisplayName("Should throw an exception when trying to save an already registered cpf.")
    public void mustThrowAnExceptionWhenCpfAlreadyUsedTest() {
        // given
        UserInsertDTO user = getUserInsertDTO();

        // when
        Mockito.when(repository.existsByEmail(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(repository.existsByCpf(Mockito.anyString()))
                .thenReturn(true);

        // then
        Throwable exception = Assertions.catchThrowable(() -> service.save(user));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("CPF already registered.");
    }

    @Test
    @DisplayName("Should throw an exception when trying to save an already registered email.")
    public void mustThrowAnExceptionWhenEmailAlreadyUsedTest() {
        // given
        UserInsertDTO user = getUserInsertDTO();

        // when
        Mockito.when(repository.existsByEmail(Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(repository.existsByCpf(Mockito.anyString()))
                .thenReturn(false);

        // then
        Throwable exception = Assertions.catchThrowable(() -> service.save(user));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Email already in use.");
    }

    private static User getUser() {
        return User.builder()
                .id(1L)
                .firstName("teste")
                .lastName("teste")
                .cpf("11100011100")
                .email("teste@email.com")
                .password("dkfjadlskjfdlkaf")
                .createdAt(OffsetDateTime.now())
                .role(Role.CLIENT)
                .build();
    }

    private static UserResponseDTO getUserResponseDTO() {
        return UserResponseDTO.builder()
                .id(1L)
                .firstName("teste")
                .lastName("teste")
                .email("teste@email.com")
                .role("CLIENT")
                .build();
    }

    private static UserInsertDTO getUserInsertDTO() {
        return UserInsertDTO.builder()
                .firstName("teste")
                .lastName("teste")
                .cpf("12345678911")
                .email("teste@email.com")
                .password("123456789").build();
    }

    private static UserUpdateDTO getUserUpdateDTO() {
        return UserUpdateDTO.builder()
                .firstName("new")
                .lastName("new")
                .email("new@email.com")
                .build();
    }

}