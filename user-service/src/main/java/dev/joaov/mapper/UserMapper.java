package dev.joaov.mapper;

import dev.joaov.domain.User;
import dev.joaov.request.UserPostRequest;
import dev.joaov.request.UserPutRequest;
import dev.joaov.response.UserGetResponse;
import dev.joaov.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
//    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    User toUser(UserPostRequest postRequest);

    User toUser(UserPutRequest putRequest);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponse(List<User> user);

    UserPostResponse toUserPostResponse(User user);
}
