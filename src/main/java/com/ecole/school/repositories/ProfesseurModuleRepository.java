package com.ecole.school.repositories;

import com.ecole.school.models.Professeur;
import com.ecole.school.models.ProfesseurModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesseurModuleRepository extends JpaRepository<ProfesseurModule, Long> {
    Optional<List<ProfesseurModule>> findAllByProfesseur(Professeur professeur);
}
