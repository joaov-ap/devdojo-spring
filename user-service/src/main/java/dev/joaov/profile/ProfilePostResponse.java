package dev.joaov.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilePostResponse {
    private Long id;
    private String name;
    private String description;
}
