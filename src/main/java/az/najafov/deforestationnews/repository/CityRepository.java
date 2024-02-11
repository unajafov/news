package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.City;
import az.najafov.deforestationnews.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query(value = "select * from city where country_id = :countryId", nativeQuery = true)
    List<City> findAllByCountry(Long countryId);
}
