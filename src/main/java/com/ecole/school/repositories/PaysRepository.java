package com.ecole.school.repositories;

import com.ecole.school.models.Pays;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaysRepository extends JpaRepository<Pays, Long> {
    
}
