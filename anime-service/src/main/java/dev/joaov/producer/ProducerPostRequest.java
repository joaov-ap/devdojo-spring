package dev.joaov.producer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
