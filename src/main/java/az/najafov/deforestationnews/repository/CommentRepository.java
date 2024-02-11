package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.Comment;
import az.najafov.deforestationnews.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comment where news_id = :newsId" ,nativeQuery = true)
    List<Comment> findAllByNews(Long newsId);

}
