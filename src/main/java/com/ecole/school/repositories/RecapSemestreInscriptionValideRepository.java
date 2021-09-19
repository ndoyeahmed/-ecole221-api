package com.ecole.school.repositories;

import com.ecole.school.models.Inscription;
import com.ecole.school.models.RecapSemestreInscriptionValide;
import com.ecole.school.models.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecapSemestreInscriptionValideRepository extends JpaRepository<RecapSemestreInscriptionValide, Long> {

  Optional<List<RecapSemestreInscriptionValide>> findAllByInscription(Inscription inscription);

  Optional<RecapSemestreInscriptionValide> findBySemestreAndInscription(Semestre semestre, Inscription inscription);
}
