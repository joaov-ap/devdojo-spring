package dev.joaov.repository;

import dev.joaov.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    private List<User> userList = new ArrayList<>();

    {
        var will = User.builder().id(1L).firstName("Will").lastName("Alcantara").email("will@gmail.com").createdAt(LocalDateTime.now()).build();
        var joao = User.builder().id(2L).firstName("Joao").lastName("Vitor").email("joao@gmail.com").createdAt(LocalDateTime.now()).build();
        var joaoA = User.builder().id(3L).firstName("Joao").lastName("Almeida").email("joaoA@gmail.com").createdAt(LocalDateTime.now()).build();
        var maria = User.builder().id(4L).firstName("Maria").lastName("Almeida").email("maria@gmail.com").createdAt(LocalDateTime.now()).build();
        userList.addAll(List.of(will, joao, joaoA, maria));
    }

    public List<User> getUserList() {
        return userList;
    }
}
