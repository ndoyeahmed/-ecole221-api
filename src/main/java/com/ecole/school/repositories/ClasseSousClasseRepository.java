package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Classe;
import com.ecole.school.models.ClasseSousClasse;
import com.ecole.school.models.SousClasse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseSousClasseRepository extends JpaRepository<ClasseSousClasse, Long> {
    Optional<List<ClasseSousClasse>> findAllByClasseAndArchiveFalse(Classe classe);

    Optional<List<ClasseSousClasse>> findAllBySousClasseAndArchiveFalse(SousClasse sousClasse);

    Optional<ClasseSousClasse> findByClasseAndSousClasseAndArchiveFalse(Classe classe, SousClasse sousClasse);
}
