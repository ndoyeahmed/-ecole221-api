package com.ecole.school.repositories;

import java.util.Optional;

import com.ecole.school.models.Profil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {
    Optional<Profil> findByLibelle(String libelle);
}
