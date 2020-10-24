package com.ecole.school.repositories;

import com.ecole.school.models.DocumentParNiveau;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentParNiveauRepository extends JpaRepository<DocumentParNiveau, Long> {
    
}
