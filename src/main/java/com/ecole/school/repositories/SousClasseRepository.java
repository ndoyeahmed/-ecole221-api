package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Niveau;
import com.ecole.school.models.SousClasse;
import com.ecole.school.models.Specialite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SousClasseRepository extends JpaRepository<SousClasse, Long> {
    Optional<List<SousClasse>> findAllByArchiveFalse();

    Optional<List<SousClasse>> findAllByNiveauAndArchiveFalse(Niveau niveau);
    
    Optional<List<SousClasse>> findAllBySpecialiteAndArchiveFalse(Specialite specialite);

    Optional<List<SousClasse>> findAllByNiveauAndSpecialiteAndArchiveFalse(Niveau niveau, Specialite specialite);
}
