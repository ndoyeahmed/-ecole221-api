package com.ecole.school.repositories;

import com.ecole.school.models.EmargementProfesseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmargementProfesseurRepository extends JpaRepository<EmargementProfesseur, Long> {
}