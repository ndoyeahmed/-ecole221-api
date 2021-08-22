package com.ecole.school.repositories;

import com.ecole.school.models.Devoir;
import com.ecole.school.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevoirRepository extends JpaRepository<Devoir, Long> {

    Optional<List<Devoir>> findAllByNoteAndArchiveFalse(Note note);
}
