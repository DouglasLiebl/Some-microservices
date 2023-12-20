package io.github.douglasliebl.msusers.service;

import io.github.douglasliebl.msusers.dto.UserDTO;
import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import io.github.douglasliebl.msusers.dto.UserResponseDTO;
import io.github.douglasliebl.msusers.dto.UserUpdateDTO;
import io.github.douglasliebl.msusers.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;

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

    @Test
    @DisplayName("Should get paginated users.")
    public void getPagedUsersTest() {
        // given
        User user = getUser();
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<User> page = new PageImpl<>(Collections.singletonList(user), pageRequest, 1);

        // when
        Mockito.when(repository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        // then
        Page<User> response = service.find(pageRequest);

        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent()).isEqualTo(Collections.singletonList(user));
        assertThat(response.getPageable().getPageNumber()).isEqualTo(page.getNumber());
        assertThat(response.getPageable().getPageSize()).isEqualTo(page.getSize());
    }

    @Test
    @DisplayName("Should successfully ban an user.")
    public void banUserTest() {
        // given
        User user = getUser();

        // when
        Mockito.when(repository.findByEmail(Mockito.anyString()))
                        .thenReturn(Optional.ofNullable(user));

        assertDoesNotThrow(() -> service.banUser(getUser().getEmail()));

        // then
        Mockito.verify(repository, Mockito.times(1)).delete(user);
    }

    @Test
    @DisplayName("Should throw an exception when trying ban an user not registered.")
    public void banUserNotRegisteredTest() {
        // given
        User user = getUser();

        // when
        Mockito.when(repository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

       Throwable exception = Assertions.catchThrowable(() -> service.banUser(getUser().getEmail()));

        // then
        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found.");
        Mockito.verify(repository, Mockito.never()).delete(user);
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

    private static UserInsertDTO getUserInsertDTO() {
        return UserInsertDTO.builder()
                .firstName("teste")
                .lastName("teste")
                .cpf("12345678911")
                .email("teste@email.com")
                .password("123456789").build();
    }

}