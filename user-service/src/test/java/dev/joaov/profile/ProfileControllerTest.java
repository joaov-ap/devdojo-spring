package dev.joaov.profile;

import dev.joaov.commons.FileUtils;
import dev.joaov.commons.ProfileUtils;
import dev.joaov.domain.Profile;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"dev.joaov.profile", "dev.joaov.commons"})
class ProfileControllerTest {
    private static final String URL = "/v1/profiles";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProfileUtils profileUtils;
    @MockitoBean
    private ProfileRepository repository;
    private List<Profile> profileList;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("GET v1/profiles findAll returns all Profiles when name is null")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);
        var response = fileUtils.readResourceFile("profile/get-profile-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles?name=Ging Freecss findAll returns found Profiles when name is not null")
    @Order(2)
    void findAll_ReturnsFoundProfiles_WhenNameIsNotNull() throws Exception {
        var response = fileUtils.readResourceFile("profile/get-profile-ging-name-200.json");
        var name = "Ging Freecss";
        var ging = profileList.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(ging));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles?name=x findAll returns empty profile list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyProfileList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("profile/get-profile-x-name-200.json");
        var name = "x";

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles/1 findById returns an profile when id is found")
    @Order(4)
    void findById_ReturnsAnProfile_WhenIdIsFound() throws Exception {
        var response = fileUtils.readResourceFile("profile/get-profile-by-id-200.json");
        var id = 1L;
        var profileFound = profileList.stream().filter(p -> p.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(profileFound);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles/99 findById throws NotFoundException when id is not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("profile/get-profile-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/profiles save creates an user")
    @Order(6)
    void save_CreatesAnUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var response = fileUtils.readResourceFile("profile/post-response-profile-201.json");
        var profileToSave = profileUtils.newProfileToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles return bad request when fields are invalid")
    @Order(7)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

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

    private static Stream<Arguments> postProfileBadRequestSource() {
        var allErrors = requiredErros();

        return Stream.of(
                Arguments.of("post-request-profile-blank-fields-400.json", allErrors),
                Arguments.of("post-request-profile-empty-fields-400.json", allErrors)
        );
    }

    private static List<String> requiredErros() {
        var nameRequiredError = "The field 'name' is required";
        var descriptionRequiredError = "The field 'description' is required";

        return new ArrayList<>(List.of(nameRequiredError, descriptionRequiredError));
    }
}