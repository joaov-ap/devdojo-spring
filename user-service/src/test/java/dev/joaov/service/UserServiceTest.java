package dev.joaov.service;

import dev.joaov.commons.UserUtils;
import dev.joaov.domain.User;
import dev.joaov.repository.UserHardCodedRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @InjectMocks
    private UserUtils userUtils;
    @Mock
    private UserHardCodedRepository repository;
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
        BDDMockito.when(repository.findByName(userExpected.getFirstName())).thenReturn(usersFound);

        var users = service.findAll(userExpected.getFirstName());
        Assertions.assertThat(users).isNotNull().containsAll(usersFound);
    }

    @Test
    @DisplayName("findById return an user with given id when user is found")
    @Order(2)
    void findById_ReturnUser_WhenIdIsFound() {
        var userExpected = userList.getFirst();
        BDDMockito.when(repository.findById(userExpected.getId())).thenReturn(Optional.of(userExpected));

        var user = service.findByIdOrThrowNotFound(userExpected.getId());
        Assertions.assertThat(user).isEqualTo(userExpected);
    }

}