package dev.joaov.service;

import dev.joaov.commons.AnimeUtils;
import dev.joaov.domain.Anime;
import dev.joaov.repository.AnimeHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;
    @InjectMocks
    private AnimeUtils animeUtils;
    @Mock
    private AnimeHardCodedRepository repository;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll return a list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnAllAnimes_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var animes = service.findAll(null);
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAll return a list with anime when argument is not null")
    @Order(2)
    void findAll_ReturnAnime_WhenNameIsNotNull() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(Collections.singletonList(anime));

        var animeFound = service.findAll(anime.getName());
        Assertions.assertThat(animeFound).isNotNull().containsAll(animeFound);
    }

    @Test
    @DisplayName("findAll returns empty list when anime is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenAnimeIsNotFound() {
        var anime = "Pluto";
        BDDMockito.when(repository.findByName(anime)).thenReturn(Collections.emptyList());

        var animes = service.findAll(anime);
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("findById returns a anime with given id")
    @Order(4)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        var anime = animeList.getFirst();
        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));

        var expectedAnime = service.findByIdOrThrowNotFound(anime.getId());
        Assertions.assertThat(expectedAnime).isEqualTo(anime);
    }

    @Test
    @DisplayName("findById Throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(ArgumentMatchers.anyLong()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSuccessful() {
        var animeToSave = Anime.builder().id(15L).name("Pluto").build();
        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        var animeSaved = service.save(animeToSave);
        Assertions.assertThat(animeSaved).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a anime")
    @Order(7)
    void delete_RemoveAnime_WhenSuccessful() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete Throws ResponseStatusException when anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(ArgumentMatchers.anyLong()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a anime")
    @Order(9)
    void update_UpdatesAnime_WhenSuccessful() {
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Pluto");

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.doNothing().when(repository).update(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update Throws ResponseStatusException when anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToUpdate = animeList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}