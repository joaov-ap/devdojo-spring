package dev.joaov.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
