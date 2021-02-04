package com.ecole.school.repositories;

import com.ecole.school.models.TypeReglement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeReglementRepository extends JpaRepository<TypeReglement, Long> {
    
}
