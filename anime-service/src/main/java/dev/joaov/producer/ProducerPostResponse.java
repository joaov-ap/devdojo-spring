package dev.joaov.producer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProducerPostResponse {
    private Long id;
    private String name;
}
