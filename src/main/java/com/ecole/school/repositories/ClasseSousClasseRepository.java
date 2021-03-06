package com.ecole.school.repositories;

import com.ecole.school.models.ClasseSousClasse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseSousClasseRepository extends JpaRepository<ClasseSousClasse, Long> {
    
}
