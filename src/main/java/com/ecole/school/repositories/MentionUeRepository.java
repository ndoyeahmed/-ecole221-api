package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.MentionUE;
import com.ecole.school.models.UE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentionUeRepository extends JpaRepository<MentionUE, Long> {
    Optional<List<MentionUE>> findAllByUeAndArchiveFalse(UE ue);
}
