package dev.joaov.repository;

import dev.joaov.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {
    private final List<Producer> producers = new ArrayList<>();

    {
        var madHouse = Producer.builder().id(1L).name("MadHouse").createdAt(LocalDateTime.now()).build();
        var toeiAnimation = Producer.builder().id(2L).name("Toei Animation").createdAt(LocalDateTime.now()).build();
        var mappa = Producer.builder().id(3L).name("Mappa").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(madHouse, toeiAnimation, mappa));
    }

    public List<Producer> getProducers() {
        return producers;
    }
}
