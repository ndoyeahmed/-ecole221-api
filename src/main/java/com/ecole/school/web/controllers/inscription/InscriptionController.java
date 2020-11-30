package com.ecole.school.web.controllers.inscription;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Document;
import com.ecole.school.models.DocumentParEtudiant;
import com.ecole.school.models.Etudiant;
import com.ecole.school.models.EtudiantTuteur;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.Profil;
import com.ecole.school.models.Utilisateur;
import com.ecole.school.services.inscription.InscriptionService;
import com.ecole.school.services.parametrages.ParametrageBaseService;
import com.ecole.school.services.parametrages.ParametrageClasseService;
import com.ecole.school.services.security.UtilisateurService;
import com.ecole.school.services.utils.Utils;
import com.ecole.school.web.POJO.InscriptionPOJO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/inscription/")
public class InscriptionController {

    private InscriptionService inscriptionService;
    private ParametrageBaseService parametrageBaseService;
    private ParametrageClasseService parametrageClasseService;
    private UtilisateurService utilisateurService;
    private Utils utils;

    @Autowired
    public InscriptionController(InscriptionService inscriptionService, ParametrageBaseService ParametrageBaseService,
        ParametrageClasseService parametrageClasseService, UtilisateurService utilisateurService, Utils utils) {

            this.inscriptionService = inscriptionService;
            this.parametrageClasseService = parametrageClasseService;
            this.parametrageBaseService = ParametrageBaseService;
            this.utilisateurService = utilisateurService;
            this.utils = utils;
    }
    
    // -------------Inscription ENDPOINTS
    @PostMapping("inscription")
    public ResponseEntity<?> inscription(@RequestBody InscriptionPOJO inscriptionPOJO) {
        
        // bloc to add etudiant
        Etudiant etudiant = inscriptionPOJO.getEtudiant();
        etudiant = inscriptionService.addEtudiant(etudiant);
        // end bloc to add etudiant

        // bloc to add inscription
        Inscription inscription = inscriptionPOJO.getInscription();
        inscription.setAnneeScolaire(parametrageBaseService.findAnneeScolaireEnCours());
        inscription.setArchive(false);
        inscription.setDate(Timestamp.valueOf(LocalDateTime.now()));
        inscription.setEtudiant(etudiant);
        inscription.setSousClasse(inscriptionPOJO.getSousClasse());

        inscription = inscriptionService.addInscription(inscription);
        // end bloc to add inscription

        // bloc to add Parent
        Profil profil = utilisateurService.findProfilByLibelle("PARENT");
        Utilisateur mere = inscriptionPOJO.getMere();
        if (mere != null && mere.getCin() != null) {
            mere.setProfil(profil);
            mere.setArchive(false);
            mere.setLogin("mere");
            mere.setPassword(utils.createCode(mere.getNom().trim()));
            mere = utilisateurService.addUser(mere);
            EtudiantTuteur etudiantTuteur = new EtudiantTuteur();
            etudiantTuteur.setEtudiant(etudiant);
            etudiantTuteur.setTuteur(mere);
            inscriptionService.addEtudiantTuteur(etudiantTuteur);
        }
        Utilisateur pere = inscriptionPOJO.getPere();
        if (pere != null && pere.getCin() != null) {
            pere.setProfil(profil);
            pere.setArchive(false);
            pere.setLogin("pere");
            pere.setPassword(utils.createCode(pere.getNom().trim()));
            pere = utilisateurService.addUser(pere);
            EtudiantTuteur etudiantTuteur = new EtudiantTuteur();
            etudiantTuteur.setEtudiant(etudiant);
            etudiantTuteur.setTuteur(pere);
            inscriptionService.addEtudiantTuteur(etudiantTuteur);
        }
        Utilisateur tuteur = inscriptionPOJO.getTuteur();
        if (tuteur != null && tuteur.getCin() != null) {
            tuteur.setProfil(profil);
            tuteur.setArchive(false);
            tuteur.setLogin("tuteur");
            tuteur.setPassword(utils.createCode(tuteur.getNom().trim()));
            tuteur = utilisateurService.addUser(tuteur);
            EtudiantTuteur etudiantTuteur = new EtudiantTuteur();
            etudiantTuteur.setEtudiant(etudiant);
            etudiantTuteur.setTuteur(tuteur);
            inscriptionService.addEtudiantTuteur(etudiantTuteur);
        }
        // end bloc to add Parent

        // bloc to add document par Etudiant
        for (Document d : inscriptionPOJO.getDocuments()) {
            DocumentParEtudiant documentParEtudiant = new DocumentParEtudiant();
            documentParEtudiant.setDocument(d);
            documentParEtudiant.setEtudiant(etudiant);
            inscriptionService.addDocumentParEtudiant(documentParEtudiant);
        }
        // end bloc to add document par etudiant

        return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
    }

    @GetMapping("inscription")
    public ResponseEntity<?> getAllInscription() {
        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireEnCours();
        return ResponseEntity.ok(inscriptionService.findAllInscriptionByAnneeScolaire(anneeScolaire));
    }
}
