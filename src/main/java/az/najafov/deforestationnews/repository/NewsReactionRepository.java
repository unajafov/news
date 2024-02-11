package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.NewsReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NewsReactionRepository extends JpaRepository<NewsReaction, Long> {

    @Query(value = "select count(nr.id) from news n left join news_reaction nr on n.id = nr.news_id " +
            "where n.id = :newsId and nr.`type` = :type", nativeQuery = true)
    int findNewsReactionCountByNewsAndType(Long newsId, String type);

//    @Query(value = "select exists(select 1  from news_reaction " +
//            "where user_id = :userId and news_id = :newsId and `type` = :type)",
//            nativeQuery = true)
//    boolean findHasReactionByNewsAndUserAndType(Long newsId, Long userId, String type);

    @Query(value = "select * from news_reaction " +
            "where user_id = :userId and news_id = :newsId and `type` = :type",
            nativeQuery = true)
    Optional<NewsReaction> findReactionByNewsAndUserAndType(Long newsId, Long userId, String type);

}
