package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<List<Document>> findAllByArchiveFalse();
}
