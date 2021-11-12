package com.ecole.school.repositories;

import com.ecole.school.models.Etudiant;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.RecapSemestreInscriptionValide;
import com.ecole.school.models.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecapSemestreInscriptionValideRepository extends JpaRepository<RecapSemestreInscriptionValide, Long> {

  Optional<List<RecapSemestreInscriptionValide>> findAllByInscription(Inscription inscription);

  @Query("select sum(r.nombreCreditSemestre) from RecapSemestreInscriptionValide r where r.inscription=:inscription")
  Optional<Integer> findSumCreditByInscription(@Param("inscription") Inscription inscription);

  Optional<RecapSemestreInscriptionValide> findBySemestreAndInscription(Semestre semestre, Inscription inscription);

  Optional<List<RecapSemestreInscriptionValide>> findAllByInscription_Etudiant(Etudiant etudiant);
}
