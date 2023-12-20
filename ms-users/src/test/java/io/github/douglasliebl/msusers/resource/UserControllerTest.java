package io.github.douglasliebl.msusers.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglasliebl.msusers.dto.*;
import io.github.douglasliebl.msusers.exception.ResourceNotFoundException;
import io.github.douglasliebl.msusers.model.entity.Role;
import io.github.douglasliebl.msusers.model.entity.User;
import io.github.douglasliebl.msusers.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)  // addFilters -> "desabilita" o spring security para os testes.
class UserControllerTest {

    static String USER_URL = "/user";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService service;

    @Test
    @DisplayName("Should create a user with success.")
    public void createUserTest() throws Exception {
        // given
        UserInsertDTO userInsertDTO = getUserInsertDTO();

        UserResponseDTO savedUser = getUserResponseDTO();

        BDDMockito.given(service.save(Mockito.any(UserInsertDTO.class)))
                .willReturn(savedUser);
        String json = new ObjectMapper().writeValueAsString(userInsertDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(USER_URL.concat("/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("firstName").value(savedUser.getFirstName()))
                .andExpect(jsonPath("lastName").value(savedUser.getLastName()))
                .andExpect(jsonPath("email").value(savedUser.getEmail()))
                .andExpect(jsonPath("role").value(savedUser.getRole()));
    }

    @Test
    @DisplayName("Should throw an exception when trying to register a new user with an already used email.")
    public void createUserWithAlreadyUsedEmailTest() throws Exception {
        // given
        UserInsertDTO userInsertDTO = getUserInsertDTO();

        BDDMockito.given(service.save(Mockito.any(UserInsertDTO.class)))
                .willThrow(new DataIntegrityViolationException("Email already in use."));
        String json = new ObjectMapper().writeValueAsString(userInsertDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(USER_URL.concat("/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("error").value("Email already in use."))
                .andExpect(jsonPath("path").value(USER_URL.concat("/register")));
    }
    @Test
    @DisplayName("Should throw an exception when trying to register a new user with an already registered cpf.")
    public void createUserWithAlreadyUsedCpfTest() throws Exception {
        // given
        UserInsertDTO userInsertDTO = getUserInsertDTO();

        BDDMockito.given(service.save(Mockito.any(UserInsertDTO.class)))
                .willThrow(new DataIntegrityViolationException("Cpf already registered."));
        String json = new ObjectMapper().writeValueAsString(userInsertDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(USER_URL.concat("/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("error").value("Cpf already registered."))
                .andExpect(jsonPath("path").value(USER_URL.concat("/register")));
    }


    @Test
    @DisplayName("Should return a user details based on Jwt token.")
    public void getMyUserDetailsTest() throws Exception {
        // given
        UserDTO userDTO = UserDTO.builder().id(1L)
                .firstName("teste")
                .lastName("teste")
                .cpf("12345678911")
                .email("teste@email.com")
                .role("CLIENT")
                .build();

        BDDMockito.given(service.me(Mockito.any()))
                .willReturn(userDTO);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(USER_URL.concat("/me"))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("cpf").value(userDTO.getCpf()))
                .andExpect(jsonPath("email").value(userDTO.getEmail()))
                .andExpect(jsonPath("role").value(userDTO.getRole()));
    }

    @Test
    @DisplayName("Should throw an exception when email is not registered.")
    public void getExceptionWhenGetMyUserDetailsTest() throws Exception {
        // given
        String email = "teste@email.com";

        BDDMockito.given(service.me(Mockito.any()))
                .willThrow(new ResourceNotFoundException("User not found with email: " + email));

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(USER_URL.concat("/me"))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("error").value("User not found with email: " + email))
                .andExpect(jsonPath("path").value("/user/me"));
    }

    @Test
    @DisplayName("Should update my user details.")
    public void updateMyUserDetailsTest() throws Exception {
        // given
        UserUpdateDTO updateData = getUserUpdateDTO();

        UserResponseDTO updatedUser = UserResponseDTO.builder()
                .id(1L)
                .firstName("new")
                .lastName("new")
                .email("new@email.com")
                .role("CLIENT")
                .build();

        String json = new ObjectMapper().writeValueAsString(updateData);

        BDDMockito.given(service.updateMyUser(Mockito.any(), Mockito.any(UserUpdateDTO.class)))
                .willReturn(updatedUser);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(USER_URL.concat("/update-data"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA")
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("role").value(updatedUser.getRole()));
    }



    @Test
    @DisplayName("Should throw an exception when try to update an user that email not found.")
    public void getAnExceptionWhenUserNotFoundTest() throws Exception {
        // given
        UserUpdateDTO insertData = getUserUpdateDTO();
        String json = new ObjectMapper().writeValueAsString(insertData);

        BDDMockito.given(service.updateMyUser(Mockito.any(), Mockito.any(UserUpdateDTO.class)))
                .willThrow(new ResourceNotFoundException("User not found with email: " + insertData.getEmail()));

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(USER_URL.concat("/update-data"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA")
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("error").value("User not found with email: " + insertData.getEmail()))
                .andExpect(jsonPath("path").value("/user/update-data"));
    }

    @Test
    @DisplayName("Should throw an exception when trying to pass an already used email to an update.")
    public void getAnExceptionWhenEmailAlreadyExistsTest() throws Exception {
        UserUpdateDTO updateData = getUserUpdateDTO();
        String json = new ObjectMapper().writeValueAsString(updateData);

        BDDMockito.given(service.updateMyUser(Mockito.any(), Mockito.any(UserUpdateDTO.class)))
                .willThrow(new DataIntegrityViolationException("Email already registered."));

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(USER_URL.concat("/update-data"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA")
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("localDateTime").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("error").value("Email already registered."))
                .andExpect(jsonPath("path").value("/user/update-data"));
    }

    @Test
    @DisplayName("Should update successfully an password.")
    public void getSuccessUpdatingPasswordTest() throws Exception {
        // given
        UserPasswordUpdateDTO password = UserPasswordUpdateDTO.builder()
                .password("teste").build();

        String response = "Password successfully updated.";
        String json = new ObjectMapper().writeValueAsString(password);


        BDDMockito.given(service.updateMyPassword(Mockito.any(), Mockito.any(UserPasswordUpdateDTO.class)))
                .willReturn(response);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(USER_URL.concat("/update-password")).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA")
                .content(json);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    @DisplayName("Should return all users paginated.")
    public void getAllUsersPaginatedTest() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .role(Role.CLIENT).build();

        BDDMockito.given(service.find(Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<User>(Collections.singletonList(user), PageRequest.of(0, 10), 1));

        String query = "?page=0&size=10";

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(USER_URL.concat(query))
                .accept(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    @Test
    @DisplayName("Should successfully delete an user")
    public void deleteMyUserTest() throws Exception {
        // given
        String response = "You account was successfully deleted.";

        BDDMockito.given(service.delete(Mockito.any()))
                .willReturn(response);

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(USER_URL.concat("/delete"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    @DisplayName("Should ban an user")
    public void banUserTest() throws Exception {
        // given
        String email = "teste@email.com";

        // when
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(USER_URL.concat("/delete/" + email))
                .header(HttpHeaders.AUTHORIZATION, "Bearer 3iSRPOKdfTG47TxAOr5FWp_BVnVpueBbbUkKLVtB8VVMF9BcA");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
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