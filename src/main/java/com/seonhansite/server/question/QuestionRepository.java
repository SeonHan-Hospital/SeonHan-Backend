package com.seonhansite.server.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    Optional<Question> findBySubject(String subject);
    Optional<Question> findByAuthor(String author);
    List<Question> findBySubjectLike(String subject);
    List<Question> findByAuthorLike(String author);

    @Query(value = "SELECT * FROM question WHERE " +
            "(:author IS NULL OR author LIKE %:author%) AND " +
            "(:subject IS NULL OR subject LIKE %:subject%) AND " +
            "(:content IS NULL OR content LIKE %:content%) AND " +
            "is_deleted IS NULL " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Question> findAllByCustom(@Param("author") String author,
                                   @Param("subject") String subject,
                                   @Param("content") String content,
                                   @Param("limit") int limit,
                                   @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM question WHERE " +
            "(:author IS NULL OR author LIKE %:author%) AND " +
            "(:subject IS NULL OR subject LIKE %:subject%) AND " +
            "(:content IS NULL OR content LIKE %:content%) AND " +
            "is_deleted IS NULL",
            nativeQuery = true)
    Integer countByCustom(@Param("author") String author,
                       @Param("subject") String subject,
                       @Param("content") String content);
}
