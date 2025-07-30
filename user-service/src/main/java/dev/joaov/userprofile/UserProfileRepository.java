package dev.joaov.userprofile;

import dev.joaov.domain.User;
import dev.joaov.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH up.profile p")
    List<UserProfile> retrieveAll();

//    @EntityGraph(attributePaths = {"user", "profile"})
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();

    @Query("SELECT up.user FROM UserProfile up WHERE up.profile.id = ?1")
    List<User> findAllUsersByProfileId(Long profileId);
}
