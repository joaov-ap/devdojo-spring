package dev.joaov.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anime {
    private Long id;
    private String name;
    private static List<Anime> animes = new ArrayList<>();

    static {
        animes.addAll(List.of(new Anime(1L, "Hunter X Hunter"),
                new Anime(2L, "YuYu Hakusho"),
                new Anime(3L, "One Piece"),
                new Anime(4L, "Naruto"),
                new Anime(5L, "Bleach")
        ));
    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
