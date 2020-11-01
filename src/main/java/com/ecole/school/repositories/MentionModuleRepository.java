package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.MentionModule;
import com.ecole.school.models.Module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentionModuleRepository extends JpaRepository<MentionModule, Long> {
    Optional<List<MentionModule>> findAllByModuleAndArchiveFalse(Module module);
}