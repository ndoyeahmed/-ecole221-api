package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.AnneeScolaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnneeScolaireRepository extends JpaRepository<AnneeScolaire, Long> {
    Optional<AnneeScolaire> findByEnCoursIsTrue();
    Optional<List<AnneeScolaire>> findAllByArchiveFalse();
}
