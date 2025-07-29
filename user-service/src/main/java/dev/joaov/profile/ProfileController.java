package dev.joaov.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll(@RequestParam(required = false) String name) {
        var profiles = service.findAll(name);
        var profileGetResponse = mapper.toProfileGetResponse(profiles);

        return ResponseEntity.ok(profileGetResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileGetResponse> findById(@PathVariable Long id) {
        var profile = service.findById(id);
        var profileGetResponse = mapper.toProfileGetResponse(profile);

        return ResponseEntity.ok(profileGetResponse);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest postRequest) {
        var profile = mapper.toProfile(postRequest);
        var profileSaved = service.save(profile);
        var profilePostResponse = mapper.toProfilePostResponse(profileSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(profilePostResponse);
    }
}
