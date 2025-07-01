package dev.joaov.controller;

import dev.joaov.mapper.UserMapper;
import dev.joaov.request.UserPostRequest;
import dev.joaov.request.UserPutRequest;
import dev.joaov.response.UserGetResponse;
import dev.joaov.response.UserPostResponse;
import dev.joaov.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String name) {
        var users = service.findAll(name);
        var userGetResponse = mapper.toUserGetResponse(users);
        return ResponseEntity.ok(userGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        var user = service.findByIdOrThrowNotFound(id);
        var userGetResponse = mapper.toUserGetResponse(user);
        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
        var user = mapper.toUser(userPostRequest);
        var userToSave = service.save(user);
        var userPostResponse = mapper.toUserPostResponse(userToSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest userPutRequest) {
        var user = mapper.toUser(userPutRequest);
        service.update(user);

        return ResponseEntity.noContent().build();
    }
}
