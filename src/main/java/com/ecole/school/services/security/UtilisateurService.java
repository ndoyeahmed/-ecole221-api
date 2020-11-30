package com.ecole.school.services.security;
import com.ecole.school.models.Profil;
import com.ecole.school.models.Utilisateur;
import com.ecole.school.repositories.ProfilRepository;
import com.ecole.school.repositories.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;
    private ProfilRepository profilRepository;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, ProfilRepository profilRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.profilRepository = profilRepository;
    }

    public Utilisateur addUser(Utilisateur utilisateur) {
        try {
            utilisateurRepository.save(utilisateur);
            return utilisateur;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Profil findProfilById(Long id) {
        return profilRepository.findById(id).orElse(null);
    }

    public Profil findProfilByLibelle(String libelle) {
        return profilRepository.findByLibelle(libelle).orElse(null);
    }
}
