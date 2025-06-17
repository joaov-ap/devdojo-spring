package dev.joaov.mapper;

import dev.joaov.domain.Anime;
import dev.joaov.domain.Producer;
import dev.joaov.request.AnimePutRequest;
import dev.joaov.request.ProducerPostRequest;
import dev.joaov.request.ProducerPutRequest;
import dev.joaov.response.ProducerGetResponse;
import dev.joaov.response.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1000))")
    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest putRequest);

    ProducerGetResponse toProducerGetResponse(Producer producer);
    ProducerPostResponse toProducerPostResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponse(List<Producer> producer);
}
