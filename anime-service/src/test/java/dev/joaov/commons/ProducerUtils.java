package dev.joaov.commons;

import dev.joaov.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {
    public List<Producer> newProducerList() {
        var dateTime = "2025-06-25T10:43:33.251820996";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();
        return new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }

    public Producer newProducerToSave() {
        return Producer.builder().id(99L).name("MadHouse").createdAt(LocalDateTime.now()).build();
    }
}
