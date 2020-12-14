package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Inscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    Optional<List<Inscription>> findAllByAnneeScolaireAndArchiveFalse(AnneeScolaire anneeScolaire);

    @Query("select i from Inscription i where i.archive=false and i.etudiant.id=:idEtudiant")
    Optional<List<Inscription>> findAllByEtudiantAndArchiveFalse(@Param("idEtudiant") Long idEtudiant);
}
