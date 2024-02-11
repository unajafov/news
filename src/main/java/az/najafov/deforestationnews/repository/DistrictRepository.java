package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query(value = "select * from district where city_id = :cityId",nativeQuery = true)
    List<District> findAllByCity(Long cityId);

}
