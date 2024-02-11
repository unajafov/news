package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles","roles.ignoredPermissions"})
    Optional<User> findByUsername(String username);

}
