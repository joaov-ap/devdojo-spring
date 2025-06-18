package dev.joaov.repository;

import dev.joaov.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        var hunterXHunter = Anime.builder().id(1L).name("Hunter X Hunter").build();
        var yuYuHakusho = Anime.builder().id(2L).name("YuYu Hakusho").build();
        var onePiece = Anime.builder().id(3L).name("One Piece").build();
        var naruto = Anime.builder().id(4L).name("Naruto").build();
        var bleach = Anime.builder().id(5L).name("Bleach").build();
        animes.addAll(List.of(hunterXHunter, yuYuHakusho, onePiece, naruto, bleach));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
