package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.Jour;
import com.ecole.school.models.Presence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {
    Optional<Presence> findByInscriptionAndJour(Inscription inscription, Jour jour);

    @Query("select p from Presence p where p.inscription=:inscription and p.etat=:etat and p.inscription.anneeScolaire=:anneeScolaire")
    Optional<List<Presence>> findAllByInscriptionAndEtatAndAnneeScolaire(@Param("inscription") Inscription inscription,
            @Param("etat") boolean etat, @Param("anneeScolaire") AnneeScolaire anneeScolaire);
}
