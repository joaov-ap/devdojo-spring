package dev.joaov.controller;

import dev.joaov.config.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1/connections")
public class ConnectionController {
    private final Connection connection;

    @GetMapping
    public ResponseEntity<Connection> getConnections() {
        log.info("Connection details: {}", connection);
        return ResponseEntity.ok(connection);
    }
}
