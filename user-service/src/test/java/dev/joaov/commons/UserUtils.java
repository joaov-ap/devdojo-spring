package dev.joaov.commons;

import dev.joaov.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public List<User> newUserList() {
        var ging = User.builder().id(1L).firstName("Ging").lastName("Freecss").email("ging@gmail.com").build();
        var isaac = User.builder().id(2L).firstName("Isaac").lastName("Netero").email("isaac@gmail.com").build();
        var beyond = User.builder().id(3L).firstName("Beyond").lastName("Netero").email("beyond@gmail.com").build();
        var linne = User.builder().id(4L).firstName("Linne").lastName("Horsdoeuvre").email("linne@gmail.com").build();
        return new ArrayList<>(List.of(ging, isaac, beyond, linne));
    }

    public User newUserToSave() {
        return User.builder().id(10L).firstName("Gon").lastName("Freecss").email("gon@gmail.com").build();
    }

    public User newUserRepoToSave() {
        return User.builder().firstName("Gon").lastName("Freecss").email("gon@gmail.com").build();
    }
}
