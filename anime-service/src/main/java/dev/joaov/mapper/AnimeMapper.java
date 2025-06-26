package dev.joaov.mapper;

import dev.joaov.domain.Anime;
import dev.joaov.request.AnimePostRequest;
import dev.joaov.request.AnimePutRequest;
import dev.joaov.response.AnimeGetResponse;
import dev.joaov.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest putRequest);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    AnimePostResponse toAnimePostResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponse(List<Anime> animes);
}
