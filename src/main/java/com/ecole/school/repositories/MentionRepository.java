package com.ecole.school.repositories;

import java.util.List;
import java.util.Optional;

import com.ecole.school.models.Domaine;
import com.ecole.school.models.Mention;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {
    Optional<List<Mention>> findAllByArchiveFalse();
    Optional<List<Mention>> findAllByDomaine(Domaine domaine);
}
