package az.najafov.deforestationnews.repository;

import az.najafov.deforestationnews.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comment where news_id = :newsId", nativeQuery = true)
    List<Comment> findAllByNews(Long newsId);

    @Query(value = "select * from comment where user_id = :userId and id = :commentId", nativeQuery = true)
    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);

}
