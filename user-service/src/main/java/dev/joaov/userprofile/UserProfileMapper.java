package dev.joaov.userprofile;

import dev.joaov.domain.User;
import dev.joaov.domain.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {
    List<UserProfileGetResponse> toUserProfileGetResponses(List<UserProfile> userProfiles);
    List<UserProfileUserGetResponse> toUserProfileUserGetResponses(List<User> users);
}
