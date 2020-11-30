package com.ecole.school.repositories;

import com.ecole.school.models.EtudiantTuteur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantTuteurRepository extends JpaRepository<EtudiantTuteur, Long> {
    
}
