package com.seonhansite.server.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findBySubject(String subject);
    Optional<Question> findByAuthor(String author);
    List<Question> findBySubjectLike(String subject);
    List<Question> findByAuthorLike(String author);
}