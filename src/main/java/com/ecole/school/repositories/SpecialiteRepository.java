package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Specialite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    Optional<List<Specialite>> findAllByArchiveFalse();

    Optional<Specialite> findByLibelleAndArchiveFalse(String libelle);
}
