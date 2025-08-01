package dev.joaov.anime;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnimePostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
