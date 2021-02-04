package com.ecole.school.repositories;

import com.ecole.school.models.Frais;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {
    
}
