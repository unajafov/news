package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
