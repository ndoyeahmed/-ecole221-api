package com.ecole.school.repositories;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.Note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<List<Note>> findAllByInscription(Inscription inscription);

    Optional<List<Note>> findAllByInscription_AnneeScolaireAndInscription_ArchiveFalse(AnneeScolaire anneeScolaire);
}
