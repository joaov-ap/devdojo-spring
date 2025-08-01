package dev.joaov.userprofile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        var profiles = service.findAll();
        var userProfileGetResponses = mapper.toUserProfileGetResponses(profiles);

        return ResponseEntity.ok(userProfileGetResponses);
    }

    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findByProfileId(@PathVariable Long id) {
        var profiles = service.findAllUsersByProfileId(id);
        var userProfileUserGetResponses = mapper.toUserProfileUserGetResponses(profiles);

        return ResponseEntity.ok(userProfileUserGetResponses);
    }
}
