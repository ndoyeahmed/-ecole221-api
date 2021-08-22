package com.ecole.school.repositories;

import com.ecole.school.models.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteProgrammeModuleRepository extends JpaRepository<NoteProgrammeModule, Long> {
    Optional<List<NoteProgrammeModule>> findAllByNote_Inscription_SousClasse(SousClasse sousClasse);

    Optional<NoteProgrammeModule> findByProgrammeModuleAndNote_Inscription(ProgrammeModule programmeModule, Inscription inscription);

    Optional<List<NoteProgrammeModule>> findAllByProgrammeModuleAndNote_Inscription_AnneeScolaire(ProgrammeModule programmeModule, AnneeScolaire anneeScolaire);

    Optional<List<NoteProgrammeModule>> findAllByNote_InscriptionAndNote_Inscription_AnneeScolaireAndProgrammeModule_ProgrammeUE_ReferentielAndProgrammeModule_ProgrammeUE_Semestre
      (Inscription inscription, AnneeScolaire anneeScolaire, Referentiel referentiel, Semestre semestre);

    Optional<List<NoteProgrammeModule>> findAllByNote_Inscription(Inscription inscription);
}
