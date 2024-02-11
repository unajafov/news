package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query(value = "select * from region where country_id = :countryId", nativeQuery = true)
    List<Region> findAllByCountry(Long countryId);

    @Query(value = "select * from region where city_id = :cityId", nativeQuery = true)
    List<Region> findAllByCity(Long cityId);

    @Query(value = "select * from region where district_id = :districtId", nativeQuery = true)
    List<Region> findAllByDistrict(Long districtId);

}
