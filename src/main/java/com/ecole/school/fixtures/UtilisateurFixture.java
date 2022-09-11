package com.ecole.school.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecole.school.models.Profil;
import com.ecole.school.models.Utilisateur;
import com.ecole.school.services.userconfig.UtilisateurService;

@Service
public class UtilisateurFixture {
    private UtilisateurService utilisateurService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
	  this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	  // $2a$10$dQWHrvibJInQdM85ZE/Ak./LD7./RAnFCD0QeBSwwvLgk9I8OxHeq
	}

    @Autowired
    public UtilisateurFixture(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    public void userFixture() {
        if (utilisateurService.findAllUserByEtat(true).size() <= 0) {
            addDefaultAdminUser();
            addDefaultSecretaireUser();
        }
    }

    public void addDefaultAdminUser() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setAdresse("Ecole 221");
        utilisateur.setCin("121212121212");
        utilisateur.setEmail("ecole221@e221.com");
        utilisateur.setEtat(true);
        utilisateur.setFonction("ecole");
        utilisateur.setLogin("ecole221");
        utilisateur.setNom("ecole221");
        utilisateur.setPrenom("ecole221");
        utilisateur.setProfession("ecole");
        utilisateur.setTelephone("338000303");
        utilisateur.setPassword(bCryptPasswordEncoder.encode("e221@2022"));

        Profil profil = utilisateurService.findProfilByLibelle("ADMIN");
        if (profil == null) {
            profil = new Profil();
            profil.setLibelle("ADMIN");
            profil = utilisateurService.addProfil(profil);
        }

        utilisateur.setProfil(profil);

        utilisateurService.addUser(utilisateur);
        System.out.println("user added successfully");
    }

    public void addDefaultSecretaireUser() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setAdresse("SEC");
        utilisateur.setCin("121212121444512");
        utilisateur.setEmail("secretaire@e221.com");
        utilisateur.setEtat(true);
        utilisateur.setFonction("ecole");
        utilisateur.setLogin("secretaire221");
        utilisateur.setNom("secretaire");
        utilisateur.setPrenom("secretaire");
        utilisateur.setProfession("ecole");
        utilisateur.setTelephone("338006303");
        utilisateur.setPassword(bCryptPasswordEncoder.encode("secretaire@2022"));

        Profil profil = utilisateurService.findProfilByLibelle("SECRETAIRE");
        if (profil == null) {
            profil = new Profil();
            profil.setLibelle("SECRETAIRE");
            profil = utilisateurService.addProfil(profil);
        }

        utilisateur.setProfil(profil);

        utilisateurService.addUser(utilisateur);
        System.out.println("user added successfully");
    }
}
