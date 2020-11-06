package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Classe;
import com.ecole.school.models.Horaire;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.Specialite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
    Optional<List<Classe>> findAllByArchiveFalse();

    Optional<List<Classe>> findAllByNiveauAndArchiveFalse(Niveau niveau);
    
    Optional<List<Classe>> findAllBySpecialiteAndArchiveFalse(Specialite specialite);

    Optional<List<Classe>> findAllByNiveauAndSpecialiteAndArchiveFalse(Niveau niveau, Specialite specialite);

    Optional<List<Classe>> findAllByHoraireAndArchiveFalse(Horaire horaire);
}
