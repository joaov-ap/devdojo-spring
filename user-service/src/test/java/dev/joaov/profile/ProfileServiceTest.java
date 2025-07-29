package dev.joaov.profile;

import dev.joaov.commons.ProfileUtils;
import dev.joaov.domain.Profile;
import dev.joaov.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {
    @InjectMocks
    private ProfileService service;
    @InjectMocks
    private ProfileUtils profileUtils;
    @Mock
    private ProfileRepository repository;
    private List<Profile> profileList;

    @BeforeEach
    void init(){
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("findAll returns all Profiles when name is null")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var profiles = service.findAll(null);
        Assertions.assertThat(profiles).isNotNull().hasSameElementsAs(profileList);
    }

    @Test
    @DisplayName("findAll returns found Profiles when name is not null")
    @Order(2)
    void findAll_ReturnsFoundProfiles_WhenNameIsNotNull() {
        var expectedProfile = profileList.getFirst();
        var profilesFound = Collections.singletonList(expectedProfile);
        BDDMockito.when(repository.findByName(expectedProfile.getName())).thenReturn(profilesFound);

        var profile = service.findAll(expectedProfile.getName());
        Assertions.assertThat(profile).isEqualTo(profilesFound);
    }

    @Test
    @DisplayName("findAll returns empty profile list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyProfileList_WhenNameIsNotFound() {
        String name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var profile = service.findAll(name);
        Assertions.assertThat(profile).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns an profile when id is found")
    @Order(4)
    void findById_ReturnsAnProfile_WhenIdIsFound() {
        var expectedProfile = profileList.getFirst();
        var id = expectedProfile.getId();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(expectedProfile));

        var profile = service.findById(id);
        Assertions.assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    @DisplayName("findById throws NotFoundException when id is not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() {
        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(id)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(6)
    void save_CreatesAnUser_WhenSuccessful() {
        var profileToSave = profileUtils.newProfileToSave();
        BDDMockito.when(repository.save(profileToSave)).thenReturn(profileToSave);

        var profileSaved = service.save(profileToSave);
        Assertions.assertThat(profileSaved).isEqualTo(profileToSave).hasNoNullFieldsOrProperties();
    }
}