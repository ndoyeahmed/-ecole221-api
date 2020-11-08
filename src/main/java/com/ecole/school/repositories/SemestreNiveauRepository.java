package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Niveau;
import com.ecole.school.models.Semestre;
import com.ecole.school.models.SemestreNiveau;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemestreNiveauRepository extends JpaRepository<SemestreNiveau, Long> {
    Optional<List<SemestreNiveau>> findAllByNiveauAndArchiveFalse(Niveau niveau);

    Optional<SemestreNiveau> findBySemestreAndNiveauAndArchiveFalse(Semestre semestre, Niveau niveau);
}
