package dev.joaov.userprofile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileUserGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
