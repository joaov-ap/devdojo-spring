package dev.joaov.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProducerPutRequest {
    private Long id;
    private String name;
}
