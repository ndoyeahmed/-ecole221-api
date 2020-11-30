package com.ecole.school.repositories;

import com.ecole.school.models.DocumentParEtudiant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentParEtudiantRepository extends JpaRepository<DocumentParEtudiant, Long> {
    
}
