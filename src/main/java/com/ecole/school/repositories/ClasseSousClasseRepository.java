package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseSousClasseRepository extends JpaRepository<ClasseSousClasse, Long> {
    Optional<List<ClasseSousClasse>> findAllByClasseAndArchiveFalse(Classe classe);

    Optional<List<ClasseSousClasse>> findAllBySousClasseAndArchiveFalse(SousClasse sousClasse);

    Optional<ClasseSousClasse> findByClasseAndSousClasseAndArchiveFalse(Classe classe, SousClasse sousClasse);

    @Query("select cs from ClasseSousClasse cs where cs.classe.horaire.id=:horaireId and cs.sousClasse.horaire.id=:horaireId and" +
            " cs.classe.id in (select cf.classe.id from ClasseReferentiel cf where cf.anneeScolaire.id=:anneeScolaireId)")
    Optional<List<ClasseSousClasse>> findAllByArchiveFalseAndHoraireAndAnneeScolaireEncours(@Param("horaireId") Long horaireId, @Param("anneeScolaireId") Long anneeScolaireId);
}
