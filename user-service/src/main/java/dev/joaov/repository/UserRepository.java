package dev.joaov.repository;

import dev.joaov.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstNameIgnoreCase(String firstName);
}
