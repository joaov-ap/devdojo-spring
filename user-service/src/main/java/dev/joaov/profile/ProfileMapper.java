package dev.joaov.profile;

import dev.joaov.domain.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    Profile toProfile(ProfilePostRequest postRequest);

    ProfileGetResponse toProfileGetResponse(Profile profile);

    List<ProfileGetResponse> toProfileGetResponse(List<Profile> profiles);

    ProfilePostResponse toProfilePostResponse(Profile profile);
}
