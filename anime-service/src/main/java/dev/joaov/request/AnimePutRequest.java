package dev.joaov.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutRequest {
    private Long id;
    private String name;
}
