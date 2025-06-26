package dev.joaov.service;

import dev.joaov.commons.ProducerUtils;
import dev.joaov.domain.Producer;
import dev.joaov.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;
    @InjectMocks
    private ProducerUtils producerUtils;
    @Mock
    private ProducerHardCodedRepository repository;
    private List<Producer> producerList;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("findAll returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var producers = service.findAll(null);
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("findAll returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundProducerInList_WhenNameIsFound() {
        var producer = producerList.getFirst();
        var expectedProducersFound = Collections.singletonList(producer);

        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(expectedProducersFound);

        var producersFound = service.findAll(producer.getName());
        Assertions.assertThat(producersFound).containsAll(expectedProducersFound);
    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var producers = service.findAll(name);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when id is not found")
    @Order(4)
    void findById_ThrowResponseStatusException_WhenIdIsNotFound() {
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findById return Producer when id is found")
    @Order(5)
    void findById_ReturnsProducer_WhenIdIsFound() {
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var producer = service.findByIdOrThrowNotFound(expectedProducer.getId());
        Assertions.assertThat(producer).isEqualTo(expectedProducer);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() {
        var producerToSave = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var savedProducer = service.save(producerToSave);
        Assertions.assertThat(savedProducer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(7)
    void delete_RemoveProducer_WhenSuccessful() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
        BDDMockito.doNothing().when(repository).delete(producerToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when id is not found")
    @Order(8)
    void delete_ThrowResponseStatusException_WhenIdIsNotFound() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(producerToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a producer")
    @Order(9)
    void update_UpdatesProducer_WhenSuccessful() {
        var producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Aniplex");

        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
        BDDMockito.doNothing().when(repository).update(producerToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when id is not found")
    @Order(10)
    void update_ThrowResponseStatusException_WhenIdIsNotFound() {
        var producerToUpdate = producerList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(producerToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}