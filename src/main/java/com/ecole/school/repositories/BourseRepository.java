package com.ecole.school.repositories;

import com.ecole.school.models.Bourse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BourseRepository extends JpaRepository<Bourse, Long> {
    
}