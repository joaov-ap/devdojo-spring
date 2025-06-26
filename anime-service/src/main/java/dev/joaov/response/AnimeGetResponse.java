package dev.joaov.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimeGetResponse {
    private Long id;
    private String name;
}
