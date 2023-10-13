package io.github.douglasliebl.msusers.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglasliebl.msusers.dto.UserDTO;
import io.github.douglasliebl.msusers.dto.UserInsertDTO;
import io.github.douglasliebl.msusers.dto.UserResponseDTO;
import io.github.douglasliebl.msusers.service.UserService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        UserResponseDTO savedUser = UserResponseDTO.builder()
                .id(1L)
                .firstName("teste")
                .lastName("teste")
                .email("teste@email.com")
                .role("CLIENT")
                .build();

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
    public void createUserWithAlreadyUsedEmail() throws Exception {
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
    public void createUserWithAlreadyUsedCpf() throws Exception {
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
    public void getMyUserDetails() throws Exception {
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

    private static UserInsertDTO getUserInsertDTO() {
        return UserInsertDTO.builder()
                .firstName("teste")
                .lastName("teste")
                .cpf("12345678911")
                .email("teste@email.com")
                .password("123456789").build();
    }

}