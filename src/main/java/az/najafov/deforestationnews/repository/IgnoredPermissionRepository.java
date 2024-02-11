package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.IgnoredPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IgnoredPermissionRepository extends JpaRepository<IgnoredPermission, Long> {
    @Query("select p from IgnoredPermission p where p.id in :ids")
    List<IgnoredPermission> findAllById(List<Long> ids);

}
