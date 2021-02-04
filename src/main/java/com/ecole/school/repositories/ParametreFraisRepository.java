package com.ecole.school.repositories;

import com.ecole.school.models.ParametreFrais;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreFraisRepository extends JpaRepository<ParametreFrais, Long> {
    
}
