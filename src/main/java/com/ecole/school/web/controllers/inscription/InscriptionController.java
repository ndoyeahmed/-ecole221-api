package com.ecole.school.web.controllers.inscription;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.ClasseReferentiel;
import com.ecole.school.models.Document;
import com.ecole.school.models.DocumentParEtudiant;
import com.ecole.school.models.Etudiant;
import com.ecole.school.models.EtudiantTuteur;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.NoteProgrammeModule;
import com.ecole.school.models.Profil;
import com.ecole.school.models.ProgrammeModule;
import com.ecole.school.models.ProgrammeUE;
import com.ecole.school.models.Utilisateur;
import com.ecole.school.services.inscription.InscriptionService;
import com.ecole.school.services.inscription.NoteService;
import com.ecole.school.services.parametrages.ParametrageBaseService;
import com.ecole.school.services.parametrages.ParametrageClasseService;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.services.userconfig.*;
import com.ecole.school.services.utils.Utils;
import com.ecole.school.web.POJO.ChangeClasse;
import com.ecole.school.web.POJO.InscriptionPOJO;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/inscription/")
public class InscriptionController {

    private InscriptionService inscriptionService;
    private ParametrageBaseService parametrageBaseService;
    private UtilisateurService utilisateurService;
    private ParametrageClasseService parametrageClasseService;
    private ParametrageReferentielService parametrageReferentielService;
    private NoteService noteService;
    private Utils utils;

    @Autowired
    public InscriptionController(InscriptionService inscriptionService, ParametrageBaseService ParametrageBaseService,
            UtilisateurService utilisateurService, Utils utils, ParametrageClasseService parametrageClasseService,
            ParametrageReferentielService parametrageReferentielService, NoteService noteService) {

        this.inscriptionService = inscriptionService;
        this.parametrageBaseService = ParametrageBaseService;
        this.utilisateurService = utilisateurService;
        this.utils = utils;
        this.parametrageClasseService = parametrageClasseService;
        this.parametrageReferentielService = parametrageReferentielService;
        this.noteService = noteService;
    }

    // -------------Inscription ENDPOINTS
    @PostMapping("inscription")
    public ResponseEntity<?> inscription(@RequestBody InscriptionPOJO inscriptionPOJO) {
        if (inscriptionPOJO == null)
            throw new BadRequestException("body required");
        if (inscriptionPOJO.getEtudiant() == null || inscriptionPOJO.getEtudiant().getCin() == null)
            throw new BadRequestException("etudiant required");
        if (inscriptionPOJO.getSousClasse() == null)
            throw new BadRequestException("sous classe required");
        if (inscriptionPOJO.getInscription() == null) {
            inscriptionPOJO.setInscription(new Inscription());
        }

        // get annee scolaire en cours
        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireEnCours();

        // bloc to add etudiant
        Etudiant etudiant = inscriptionPOJO.getEtudiant();
        if (etudiant.getId() == null && etudiant.getMatricule() == null) {
            etudiant.setMatricule(
                    utils.genereMatriculeEtudiant(inscriptionPOJO.getSousClasse().getSpecialite().getNum(),
                            anneeScolaire.getAnnee() + "", inscriptionService.findAllEtudiant()));

            etudiant = inscriptionService.addEtudiant(etudiant);

            // create compte etudiant
            Profil profilEtudiant = utilisateurService.findProfilByLibelle("ETUDIANT");
            Utilisateur etudiantUser = new Utilisateur();
            etudiantUser.setCin(etudiant.getCin());
            etudiantUser.setEmail(etudiant.getEmail());
            etudiantUser.setNom(etudiant.getNom());
            etudiantUser.setPrenom(etudiant.getPrenom());
            etudiantUser.setTelephone(etudiant.getTelephone());
            etudiantUser.setProfil(profilEtudiant);
            etudiantUser.setArchive(false);
            etudiantUser.setEtat(true);
            etudiantUser.setLogin(etudiant.getEmail());
            etudiantUser.setPassword(utils.encodePassword(utils.generatePassword()));
            etudiantUser = utilisateurService.addUser(etudiantUser);
            utilisateurService.email(etudiantUser, etudiantUser.getPassword());
            // end bloc to add etudiant

            // bloc to add Parent
            Utilisateur mere = inscriptionPOJO.getMere();
            if (mere != null && mere.getCin() != null) {
                Profil profilMere = utilisateurService.findProfilByLibelle("MERE");
                mere.setProfil(profilMere);
                mere.setArchive(false);
                mere.setLogin(mere.getNom().trim().substring(0, 1) + "mere");
                mere.setPassword(utils.encodePassword(utils.generatePassword()));
                mere = utilisateurService.addUser(mere);
                EtudiantTuteur etudiantTuteur = new EtudiantTuteur();
                etudiantTuteur.setEtudiant(etudiant);
                etudiantTuteur.setTuteur(mere);
                inscriptionService.addEtudiantTuteur(etudiantTuteur);
            }
            Utilisateur pere = inscriptionPOJO.getPere();
            if (pere != null && pere.getCin() != null) {
                Profil profilPere = utilisateurService.findProfilByLibelle("PERE");
                pere.setProfil(profilPere);
                pere.setArchive(false);
                pere.setLogin(pere.getNom().trim().substring(0, 1) + "pere");
                pere.setPassword(utils.encodePassword(utils.generatePassword()));
                pere = utilisateurService.addUser(pere);
                EtudiantTuteur etudiantTuteur = new EtudiantTuteur();
                etudiantTuteur.setEtudiant(etudiant);
                etudiantTuteur.setTuteur(pere);
                inscriptionService.addEtudiantTuteur(etudiantTuteur);
            }
            Utilisateur tuteur = inscriptionPOJO.getTuteur();
            if (tuteur != null && tuteur.getCin() != null) {
                Profil profilTuteur = utilisateurService.findProfilByLibelle("TUTEUR");
                tuteur.setProfil(profilTuteur);
                tuteur.setArchive(false);
                tuteur.setLogin(tuteur.getNom().trim().substring(0, 1) + "tuteur");
                tuteur.setPassword(utils.encodePassword(utils.generatePassword()));
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
        }

        // bloc to add inscription
        Inscription inscription = inscriptionPOJO.getInscription();
        inscription.setAnneeScolaire(anneeScolaire);
        inscription.setArchive(false);
        inscription.setDate(Timestamp.valueOf(LocalDateTime.now()));
        inscription.setEtudiant(etudiant);
        inscription.setSousClasse(inscriptionPOJO.getSousClasse());

        inscription = inscriptionService.addInscription(inscription);
        // end bloc to add inscription

        return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
    }

    @GetMapping("inscription")
    public ResponseEntity<?> getAllInscription() {
        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireEnCours();
        return ResponseEntity.ok(inscriptionService.findAllInscriptionByAnneeScolaire(anneeScolaire));
    }

    @GetMapping("inscription/{id}")
    public ResponseEntity<?> getInscriptionById(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Inscription inscription = inscriptionService.findInscriptionById(id);
        if (inscription == null)
            throw new BadRequestException("inscription not found");

        return ResponseEntity.ok(inscription);
    }

    @GetMapping("etudiant/inscriptions/{idInscription}")
    public ResponseEntity<?> getAllEtudiantInscription(@PathVariable Long idInscription) {
        if (idInscription == null)
            throw new BadRequestException("idInscription required");
        Inscription inscription = inscriptionService.findInscriptionById(idInscription);
        if (inscription == null)
            throw new BadRequestException("inscription not found");

        return ResponseEntity.ok(inscriptionService.findAllInscriptionByEtudiantId(inscription.getEtudiant().getId()));
    }

    @PutMapping("etudiant/change/classe")
    public ResponseEntity<?> changeEtudiantClasse(@RequestBody ChangeClasse changeClasse) {
        if (changeClasse.getInscription().getSousClasse().getId() != changeClasse.getSousClasse().getId()) {
            Inscription inscription = changeClasse.getInscription();
            inscription.setSousClasse(changeClasse.getSousClasse());
            inscriptionService.addInscription(inscription);

            ClasseReferentiel classeReferentiel = parametrageClasseService.hasClasseAffected(changeClasse.getNiveau(),
                    changeClasse.getSpecialite(), changeClasse.getHoraire());
            if (classeReferentiel != null && changeClasse.isRegulariser()) {
                System.out.println("in classe");
                List<ProgrammeUE> programmeUEs = parametrageReferentielService
                        .findAllProgrammeUEByReferentiel(classeReferentiel.getReferentiel());
                if (!programmeUEs.isEmpty()) {
                    System.out.println("in PU");
                    List<List<ProgrammeModule>> pLists = new ArrayList<>();
                    programmeUEs.parallelStream().forEach(x -> {
                        List<ProgrammeModule> programmeModules = parametrageReferentielService
                                .findAllProgrammeModuleByProgrammeUE(x.getId());
                        if (!programmeModules.isEmpty()) {
                            System.out.println("in PM");
                            pLists.add(programmeModules);
                        }
                    });
                    noteService.regulariseNote(pLists, inscription);
                }
                return ResponseEntity.ok(Collections.singletonMap("response", true));
            } else {
                return ResponseEntity.ok(Collections.singletonMap("response", true));
            }
        } else {
            return ResponseEntity.ok(Collections.singletonMap("response", false));
        }
    }

    @GetMapping("etudiant/cin/{cin}")
    public ResponseEntity<?> getEtudiantByCin(@PathVariable String cin) {
        if (cin == null || cin.trim().equals(""))
            throw new BadRequestException("cin required");

        return ResponseEntity.ok(inscriptionService.findEtudiantByCin(cin));
    }

    @GetMapping("utilisateur/parent/{profil}/{cin}")
    public ResponseEntity<?> getParent(@PathVariable String profil, @PathVariable String cin) {
        if (profil == null || profil.trim().equals(""))
            throw new BadRequestException("profil required");
        if (cin == null || cin.trim().equals(""))
            throw new BadRequestException("cin required");

        Profil profil1 = utilisateurService.findProfilByLibelle(profil);
        if (profil1 == null)
            throw new EntityNotFoundException("profil not found");

        return ResponseEntity.ok(utilisateurService.findUserByCinAndProfil(cin, profil1));
    }
}
