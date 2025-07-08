package dev.joaov.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
