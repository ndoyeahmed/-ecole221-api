package com.ecole.school.repositories;

import com.ecole.school.models.PlanificationCours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanificationCoursRepository extends JpaRepository<PlanificationCours, Long> {
}
