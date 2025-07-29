package dev.joaov.commons;

import dev.joaov.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileUtils {
    public List<Profile> newProfileList() {
        var ging = Profile.builder().id(1L).name("Ging Freecss").description("Strong fighter, one of the best Nen conjurators").build();
        var isaac = Profile.builder().id(2L).name("Isaac").description("Ex Hunter Association Boss, one of the strongest man in the world").build();
        var beyond = Profile.builder().id(3L).name("Beyond").description("Isaac's son, one of the best Nen conjurators").build();
        var linne = Profile.builder().id(4L).name("Linne").description("Ex partner of Isaac, one of the best Nen conjurators").build();
        return new ArrayList<>(List.of(ging, isaac, beyond, linne));
    }

    public Profile newProfileToSave() {
        return Profile.builder().id(10L).name("Gon Freecss").description("Ging's son, has a great potential").build();
    }
}
