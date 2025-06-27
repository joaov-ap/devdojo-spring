package dev.joaov.repository;

import dev.joaov.commons.UserUtils;
import dev.joaov.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserHardCodedRepositoryTest {
    @InjectMocks
    private UserHardCodedRepository repository;
    @InjectMocks
    private UserUtils userUtils;
    @Mock
    private UserData userData;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns all users when name is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenNameIsNull() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);

        var users = repository.findAll();
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns an user when id is given")
    @Order(2)
    void findById_ReturnsUser_WhenIdIsGiven() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var userExpected = userList.getFirst();

        var user = repository.findById(userExpected.getId());
        Assertions.assertThat(user).isPresent().contains(userExpected);
    }

    @Test
    @DisplayName("findByName returns a user list when name is given")
    @Order(3)
    void findByName_ReturnsUserList_WhenNameIsGiven() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var userExpected = userList.getFirst();

        var users = repository.findByName(userExpected.getFirstName());
        Assertions.assertThat(users).hasSize(1).contains(userExpected);
    }

    @Test
    @DisplayName("findByName returns an empty list when name is not found")
    @Order(4)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var userExpected = "not-found";

        var users = repository.findByName(userExpected);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save creates an user")
    @Order(5)
    void save_CreatesAnUser_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var userToSave = userUtils.newUserToSave();

        var userSaved = repository.save(userToSave);
        Assertions.assertThat(userSaved).hasNoNullFieldsOrProperties().isEqualTo(userToSave);
    }

    @Test
    @DisplayName("delete remove an user")
    @Order(6)
    void delete_RemoveAnUser_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var userToDelete = userList.getFirst();

        repository.delete(userToDelete);
        Assertions.assertThat(userList).isNotNull().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates an user")
    @Order(6)
    void update_UpdatesAnUser_WhenSuccessful() {
        BDDMockito.when(userData.getUserList()).thenReturn(userList);
        var userToUpdate = userList.getFirst();

        repository.update(userToUpdate);
        Assertions.assertThat(userList).isNotNull().contains(userToUpdate);
    }
}