package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.UE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UeRepository extends JpaRepository<UE, Long> {
    Optional<List<UE>> findAllByArchiveFalse();
}
