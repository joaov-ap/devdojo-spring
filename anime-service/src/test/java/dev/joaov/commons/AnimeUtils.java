package dev.joaov.commons;

import dev.joaov.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {
    public List<Anime> newAnimeList() {
        var hunterXHunter = Anime.builder().id(1L).name("Hunter X Hunter").build();
        var yuYuHakusho = Anime.builder().id(2L).name("YuYu Hakusho").build();
        var onePiece = Anime.builder().id(3L).name("One Piece").build();
        var naruto = Anime.builder().id(4L).name("Naruto").build();
        var bleach = Anime.builder().id(5L).name("Bleach").build();
        return new ArrayList<>(List.of(hunterXHunter, yuYuHakusho, onePiece, naruto, bleach));
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(15L).name("Pluto").build();
    }
}
