package com.ecole.school.repositories;

import com.ecole.school.models.NoteProgrammeModule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteProgrammeModuleRepository extends JpaRepository<NoteProgrammeModule, Long> {
    
}
