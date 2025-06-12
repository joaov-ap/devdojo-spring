package dev.joaov.controller;

import dev.joaov.domain.Producer;
import dev.joaov.mapper.ProducerMapper;
import dev.joaov.request.ProducerPostRequest;
import dev.joaov.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {
    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> nomeProducers(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);
        var producerGetResponse = MAPPER.toProducerGetResponse(Producer.getProducers());
        var response = producerGetResponse.stream().filter(n -> n.getName().equalsIgnoreCase(name)).toList();

        if (name == null) return ResponseEntity.ok(producerGetResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);
        var producerGetResponse = Producer.getProducers().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().map(MAPPER::toProducerGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));
        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {

        log.info("'{}'", headers);
        var producer = MAPPER.toProducer(producerPostRequest);
        var response = MAPPER.toProducerGetResponse(producer);

        Producer.getProducers().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);
        var producerToDelete = Producer.getProducers().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        Producer.getProducers().remove(producerToDelete);

        return ResponseEntity.noContent().build();
    }
}
