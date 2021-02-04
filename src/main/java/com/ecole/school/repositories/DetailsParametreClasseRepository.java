package com.ecole.school.repositories;

import com.ecole.school.models.DetailsParametreClasse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsParametreClasseRepository extends JpaRepository<DetailsParametreClasse, Long> {
    
}
