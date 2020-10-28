package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.DocumentParNiveau;
import com.ecole.school.models.Niveau;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentParNiveauRepository extends JpaRepository<DocumentParNiveau, Long> {
    Optional<List<DocumentParNiveau>> findAllByNiveauAndArchiveFalse(Niveau niveau);
}
