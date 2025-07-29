package dev.joaov.user;

import dev.joaov.commons.UserUtils;
import dev.joaov.domain.User;
import dev.joaov.exception.EmailExistsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @InjectMocks
    private UserUtils userUtils;
    @Mock
    private UserRepository repository;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns all users when name is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns users list with given name when name is not null")
    @Order(2)
    void findAll_ReturnsUsersList_WhenNameIsNotNull() {
        var userExpected = userList.getFirst();
        var usersFound = Collections.singletonList(userExpected);
        BDDMockito.when(repository.findByFirstNameIgnoreCase(userExpected.getFirstName())).thenReturn(usersFound);

        var users = service.findAll(userExpected.getFirstName());
        Assertions.assertThat(users).containsAll(usersFound);
    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(name)).thenReturn(Collections.emptyList());

        var users = service.findAll(name);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById return an user with given id when user is found")
    @Order(4)
    void findById_ReturnUser_WhenIdIsFound() {
        var userExpected = userList.getFirst();
        BDDMockito.when(repository.findById(userExpected.getId())).thenReturn(Optional.of(userExpected));

        var user = service.findByIdOrThrowNotFound(userExpected.getId());
        Assertions.assertThat(user).isEqualTo(userExpected);
    }

    @Test
    @DisplayName("findById throws NotFoundException when user id is not found")
    @Order(4)
    void findById_ThrowsNotFoundException_WhenIdIsNotFound() {
        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(id))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(5)
    void save_CreatesAnUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave();
        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);
        BDDMockito.when(repository.findByEmail(userToSave.getEmail())).thenReturn(Optional.empty());

        var userSaved = service.save(userToSave);
        Assertions.assertThat(userSaved).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes an user")
    @Order(6)
    void delete_RemovesAnUser_WhenSuccessful() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws NotFoundException when user id is not found")
    @Order(7)
    void delete_ThrowsNotFoundException_WhenIdIsNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(ArgumentMatchers.anyLong()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an user")
    @Order(8)
    void update_UpdatewsAnUser_WhenSuccessful() {
        var userToUpdate = userList.getFirst().withFirstName("Kaito");
        var id = userToUpdate.getId();
        var email = userToUpdate.getEmail();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToUpdate)).thenReturn(userToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @DisplayName("update throws NotFoundException when user id is not found")
    @Order(9)
    void update_ThrowsNotFoundException_WhenIdIsNotFound() {
        var userToUpdate = userList.getFirst();
        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update throws EmailExistsException when user email belongs to another user")
    @Order(10)
    void update_ThrowsEmailExistsException_WhenUserEmailBelongsToAnotherUser() {
        var savedUser = userList.getLast();
        var userToUpdate = userList.getFirst().withEmail(savedUser.getEmail());
        var email = userToUpdate.getEmail();
        var id = userToUpdate.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(EmailExistsException.class);
    }

    @Test
    @DisplayName("save throws EmailExistsException when email already exists")
    @Order(10)
    void save_ThrowsEmailExistsException_WhenEmailAlreadyExists() {
        var savedUser = userList.getLast();
        var userToSave = userUtils.newUserToSave().withEmail(savedUser.getEmail());
        var email = userToSave.getEmail();

        BDDMockito.when(repository.findByEmail(email)).thenReturn(Optional.of(savedUser));

        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToSave))
                .isInstanceOf(EmailExistsException.class);
    }

}