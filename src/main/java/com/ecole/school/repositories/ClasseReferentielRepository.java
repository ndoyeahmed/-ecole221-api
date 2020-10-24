package com.ecole.school.repositories;

import com.ecole.school.models.ClasseReferentiel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseReferentielRepository extends JpaRepository<ClasseReferentiel, Long> {
    
}
