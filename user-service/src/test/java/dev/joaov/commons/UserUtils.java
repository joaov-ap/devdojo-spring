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
        var dateTime = "2025-06-30T16:51:25.102379982";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ging = User.builder().id(1L).firstName("Ging").lastName("Freecss").email("ging@gmail.com").createdAt(localDateTime).build();
        var isaac = User.builder().id(2L).firstName("Isaac").lastName("Netero").email("isaac@gmail.com").createdAt(localDateTime).build();
        var beyond = User.builder().id(3L).firstName("Beyond").lastName("Netero").email("beyond@gmail.com").createdAt(localDateTime).build();
        var linne = User.builder().id(4L).firstName("Linne").lastName("Horsdoeuvre").email("Horsdoeuvre").createdAt(localDateTime).build();
        return new ArrayList<>(List.of(ging, isaac, beyond, linne));
    }

    public User newUserToSave() {
        return User.builder().id(10L).firstName("Gon").lastName("Freecss").email("gon@gmail.com").createdAt(LocalDateTime.now()).build();
    }
}
