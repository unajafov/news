package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {
}
