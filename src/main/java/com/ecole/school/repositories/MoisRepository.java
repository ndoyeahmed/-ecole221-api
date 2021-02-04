package com.ecole.school.repositories;

import com.ecole.school.models.Mois;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoisRepository extends JpaRepository<Mois, Long> {
    
}
