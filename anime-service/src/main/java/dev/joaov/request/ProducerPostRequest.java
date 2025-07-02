package dev.joaov.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
