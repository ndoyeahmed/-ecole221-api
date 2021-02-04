package com.ecole.school.repositories;

import com.ecole.school.models.ParametrePaiement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametrePaiementRepository extends JpaRepository<ParametrePaiement, Long> {
    
}
