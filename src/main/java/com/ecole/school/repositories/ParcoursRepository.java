package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Parcours;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcoursRepository extends JpaRepository<Parcours, Long> {
    Optional<List<Parcours>> findAllByArchiveFalse();
}
