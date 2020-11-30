package com.ecole.school.repositories;

import com.ecole.school.models.DocumentPourEtudiant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentPourEtudiantRepository extends JpaRepository<DocumentPourEtudiant, Long> {
    
}
