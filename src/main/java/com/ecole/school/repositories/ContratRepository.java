package com.ecole.school.repositories;

import com.ecole.school.models.Contrat;
import com.ecole.school.models.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {
    Optional<Contrat> findByEnCoursTrueAndProfesseur(Professeur professeur);
}
