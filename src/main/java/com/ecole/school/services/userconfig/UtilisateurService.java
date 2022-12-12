package com.ecole.school.services.userconfig;

import com.ecole.school.models.Profil;
import com.ecole.school.models.Utilisateur;
import com.ecole.school.repositories.ProfilRepository;
import com.ecole.school.repositories.UtilisateurRepository;
import com.ecole.school.services.MailService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class UtilisateurService {
    private UtilisateurRepository utilisateurRepository;
    private ProfilRepository profilRepository;
    private MailService mailService;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, ProfilRepository profilRepository, MailService mailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.profilRepository = profilRepository;
        this.mailService = mailService;
    }

    public Utilisateur connectedUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String login = "";
        if(authentication.getPrincipal() instanceof UserDetails){
            UserDetails user = (UserDetails)authentication.getPrincipal();
            login = user.getUsername();
        }if(authentication.getPrincipal() instanceof String)
            login = (String)authentication.getPrincipal();
        return utilisateurRepository
                .connexion(login, true)
                .map(user -> user)
                .orElse(null);
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

    public Utilisateur findUserByCin(String cin) {
        return utilisateurRepository.findByCinAndArchiveFalse(cin).orElse(null);
    }

    public void email(Utilisateur utilisateur, String password){
        try {
            String subject = "Confirmation de votre inscription";
            String body = "Bonjour " + utilisateur.getPrenom() + " " + utilisateur.getNom() + ". Vous recevez ce mail  pour confirmer votre" +
                    " inscription à l'ECOLE 221.<br/>Votre login: " + utilisateur.getLogin() + "<br/> Votre mot de passe: " + password;
            mailService.send(utilisateur.getEmail(), subject, body);
            // utilisateur.setEtat(true);
            //urepository.save(utilisateur);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Profil addProfil(Profil profil) {
        try {
            profilRepository.save(profil);
            return profil;
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

    public Utilisateur findUserByCinAndProfil(String cin, Profil profil) {
        return utilisateurRepository.findByCinAndArchiveFalseAndProfil(cin, profil).orElse(null);
    }

    public List<Utilisateur> findAllUserByEtat(Boolean etat) {
        return utilisateurRepository.findAllByEtat(etat).orElse(new ArrayList<>());
    }
}
