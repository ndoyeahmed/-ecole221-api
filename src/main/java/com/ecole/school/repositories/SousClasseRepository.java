package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Horaire;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.SousClasse;
import com.ecole.school.models.Specialite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SousClasseRepository extends JpaRepository<SousClasse, Long> {
    Optional<List<SousClasse>> findAllByArchiveFalse();

    Optional<List<SousClasse>> findAllByNiveauAndArchiveFalse(Niveau niveau);
    
    Optional<List<SousClasse>> findAllBySpecialiteAndArchiveFalse(Specialite specialite);

    Optional<List<SousClasse>> findAllByNiveauAndSpecialiteAndArchiveFalse(Niveau niveau, Specialite specialite);

    Optional<SousClasse> findByNiveauAndSpecialiteAndHoraireAndArchiveFalse(Niveau niveau, Specialite specialite, Horaire horaire);

    @Query("select sc from SousClasse sc where sc.archive=false and sc.niveau=:niveau and sc.specialite=:specialite and sc.horaire=:horaire" +
    " and sc.nbrEleve > " + 
    "(select count(*) from Inscription i where i.archive=false and i.anneeScolaire=:anneeScolaire" + 
    " and i.sousClasse.niveau=:niveau and i.sousClasse.specialite=:specialite and i.sousClasse.horaire=:horaire)")
    Optional<List<SousClasse>> findByAnneeScolaireAndNiveauAndSpecialiteAndHoraireAndArchiveFalseAndNombreInscritNotFull(@Param("niveau") Niveau niveau, 
    @Param("specialite") Specialite specialite, @Param("horaire") Horaire horaire, @Param("anneeScolaire") AnneeScolaire anneeScolaire);
}
