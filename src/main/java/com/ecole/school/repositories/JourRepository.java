package com.ecole.school.repositories;

import java.util.Date;
import java.util.Optional;

import com.ecole.school.models.Jour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourRepository extends JpaRepository<Jour, Long> {
    Optional<Jour> findByDate(Date date);
}
