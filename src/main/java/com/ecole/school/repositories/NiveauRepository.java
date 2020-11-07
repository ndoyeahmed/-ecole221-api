package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Cycle;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.Parcours;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {
    
    Optional<List<Niveau>> findAllByArchiveFalse();

    Optional<List<Niveau>> findAllByCycle(Cycle cycle);

    Optional<List<Niveau>> findAllByParcours(Parcours parcours);
}
