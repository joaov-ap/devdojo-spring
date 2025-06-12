package dev.joaov.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Producer {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private static List<Producer> producers = new ArrayList<>();

    static {
        var madHouse = Producer.builder().id(1L).name("MadHouse").createdAt(LocalDateTime.now()).build();
        var toeiAnimation = Producer.builder().id(2L).name("Toei Animation").createdAt(LocalDateTime.now()).build();
        var mappa = Producer.builder().id(3L).name("Mappa").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(madHouse, toeiAnimation, mappa));
    }

    public static List<Producer> getProducers() {
        return producers;
    }
}
