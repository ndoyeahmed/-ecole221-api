package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.NiveauSpecialite;
import com.ecole.school.models.Specialite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauSpecialiteRepository extends JpaRepository<NiveauSpecialite, Long> {
    Optional<List<NiveauSpecialite>> findAllBySpecialiteAndArchiveFalse(Specialite specialite);
}
