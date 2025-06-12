package dev.joaov.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnimeGetResponse {
    private Long id;
    private String name;
}
