package com.ecole.school.repositories;

import java.util.Optional;

import com.ecole.school.models.Profil;
import com.ecole.school.models.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    @Query("SELECT u FROM Utilisateur u WHERE u.login=:login and u.etat=:etat")
    Optional<Utilisateur> connexion(@Param("login") String login, @Param("etat") Boolean etat);

    Optional<Utilisateur> findByCinAndArchiveFalseAndProfil(String cin, Profil profil);
}
