package dev.joaov.controller;

import dev.joaov.commons.FileUtils;
import dev.joaov.commons.UserUtils;
import dev.joaov.domain.User;
import dev.joaov.repository.UserData;
import dev.joaov.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan("dev.joaov")
class UserControllerTest {
    private static final String URL = "/v1/users";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserUtils userUtils;
    @MockitoBean
    private UserRepository repository;
    @MockitoBean
    private UserData userData;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=Joao returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-ging-name-200.json");
        var name = "Ging";
        var ging = userList.stream().filter(user -> user.getFirstName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(Collections.singletonList(ging));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 returns User when id is found")
    @Order(4)
    void findById_ReturnUser_WhenIdIsFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var id = 1L;
        var firstUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(firstUser);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/99 throws NotFoundException when id is not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/users creates an user")
    @Order(6)
    void save_CreatesAnUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");
        var userToSave = userUtils.newUserToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/users updates an user")
    @Order(7)
    void update_UpdatesAnUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");
        var id = 1L;
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users throws NotFoundException when id is not found")
    @Order(8)
    void update_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");
        var response = fileUtils.readResourceFile("user/put-user-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/users/1 removes an user")
    @Order(9)
    void remove_RemovesAnUser_WhenSuccessful() throws Exception {
        var id = userList.getFirst().getId();
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users/99 throws NotFoundException when id is not found")
    @Order(10)
    void remove_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/delete-user-by-id-404.json");
        var id = 99;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/users return bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-empty-fields-400.json");

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        var firstNameError = "The field 'firstName' is required";
        var lastNameError = "The field 'lastName' is required";
        var emailError = "The field 'email' is required";

        Assertions.assertThat(resolvedException.getMessage()).contains(firstNameError, lastNameError, emailError);
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users return bad request when fields are invalid")
    @Order(12)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users return bad request when fields are invalid")
    @Order(13)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();
        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        var allErrors = requiredErrors();
        var emailInvalidError = emailInvalidError();

        return Stream.of(
                Arguments.of("post-request-user-blank-fields-400.json", allErrors),
                Arguments.of("post-request-user-empty-fields-400.json", allErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailInvalidError)
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var allErrors = requiredErrors();
        var idError = idNullError();
        allErrors.add(idError.getFirst());
        var emailInvalidError = emailInvalidError();

        return Stream.of(
                Arguments.of("put-request-user-blank-fields-400.json", allErrors),
                Arguments.of("put-request-user-empty-fields-400.json", allErrors),
                Arguments.of("put-request-user-null-id-400.json", idError),
                Arguments.of("put-request-user-invalid-email-400.json", emailInvalidError)
        );
    }

    private static List<String> idNullError() {
        var idNullError = "The field 'id' cannot be null";
        return List.of(idNullError);
    }

    private static List<String> emailInvalidError() {
        var emailInvalidError = "The email is not valid";
        return List.of(emailInvalidError);
    }

    private static List<String> requiredErrors() {
        var firstNameRequiredError = "The field 'firstName' is required";
        var lastNameRequiredError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";
        return new ArrayList<>(List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError));
    }
}