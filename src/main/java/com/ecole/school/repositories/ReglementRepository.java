package com.ecole.school.repositories;

import com.ecole.school.models.Reglement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReglementRepository extends JpaRepository<Reglement, Long> {
    
}
