package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("select n from News n left join fetch n.comments where n.id = :newsId")
    Optional<News> findByIdWithComments(Long newsId);

    @Query(value = "select * from news where news.region_id = :regionId ", nativeQuery = true)
    List<News> findAllByRegion(Long regionId);

    @Modifying
    @Query(value = "update news set view = view+1 where id =:id ", nativeQuery = true)
    void incrementViews(Long id);

}
