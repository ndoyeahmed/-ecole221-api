package com.ecole.school.repositories;

import com.ecole.school.models.Avance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvanceRepository extends JpaRepository<Avance, Long> {
    
}
