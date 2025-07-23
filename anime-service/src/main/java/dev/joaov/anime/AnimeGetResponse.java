package dev.joaov.anime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimeGetResponse {
    private Long id;
    private String name;
}
