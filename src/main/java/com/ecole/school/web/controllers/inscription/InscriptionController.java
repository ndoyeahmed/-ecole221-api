package com.ecole.school.web.controllers.inscription;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.ecole.school.models.*;
import com.ecole.school.services.inscription.InscriptionService;
import com.ecole.school.services.notes.NotesService;
import com.ecole.school.services.parametrages.ParametrageBaseService;
import com.ecole.school.services.parametrages.ParametrageClasseService;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.services.userconfig.*;
import com.ecole.school.services.utils.FileStorageService;
import com.ecole.school.services.utils.PDFGenerator;
import com.ecole.school.services.utils.Utils;
import com.ecole.school.web.POJO.ChangeClasse;
import com.ecole.school.web.POJO.InscriptionPOJO;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.EntityNotFoundException;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@CrossOrigin
@RequestMapping("/api/inscription/")
public class InscriptionController {

    private InscriptionService inscriptionService;
    private ParametrageBaseService parametrageBaseService;
    private UtilisateurService utilisateurService;
    private ParametrageClasseService parametrageClasseService;
    private ParametrageReferentielService parametrageReferentielService;
    private NotesService noteService;
    private Utils utils;
    private FileStorageService storageService;

    @Autowired
    public InscriptionController(InscriptionService inscriptionService, ParametrageBaseService ParametrageBaseService,
            UtilisateurService utilisateurService, Utils utils, ParametrageClasseService parametrageClasseService,
            ParametrageReferentielService parametrageReferentielService, NotesService noteService,
            FileStorageService storageService) {

        this.inscriptionService = inscriptionService;
        this.parametrageBaseService = ParametrageBaseService;
        this.utilisateurService = utilisateurService;
        this.utils = utils;
        this.parametrageClasseService = parametrageClasseService;
        this.parametrageReferentielService = parametrageReferentielService;
        this.noteService = noteService;
        this.storageService = storageService;
    }

    // generate ID card 
    @GetMapping(value = "/carte-etudiants",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> customersReport() throws IOException {
        List<Etudiant> etudiants = inscriptionService.findAllEtudiant();
 
        ByteArrayInputStream bis = PDFGenerator.customerPDFReport(etudiants);
 
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=customers.pdf");
 
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // -------------Inscription ENDPOINTS
    @PostMapping("inscription")
    public ResponseEntity<?> inscription(@RequestBody InscriptionPOJO inscriptionPOJO)
            throws FileNotFoundException, IOException, InterruptedException {
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

            // upload photo profil etudiant
            if (etudiant.getPhoto() != null && !etudiant.getPhoto().trim().equals("")) {
                String docName = etudiant.getMatricule();
                String resp = etudiant.getPhoto();
                resp = resp.replace("data:image/jpeg;base64,", "");
                byte[] imageByte = Base64.getMimeDecoder().decode(resp.trim().split(",")[0]);
                String filename = "/photo_profil_etudiants/" + docName + ".jpg";

                String dossier = System.getProperty("user.home") + "/ecole221files/";

                if (etudiant.getFileType().equals("image/png")) {
                    filename = "/photo_profil_etudiants/" + docName + ".png";
                } else if (etudiant.getFileType().equals("image/jpg")) {
                    filename = "/photo_profil_etudiants/" + docName + ".jpg";
                } else if (etudiant.getFileType().equals("image/jpeg")) {
                    filename = "/photo_profil_etudiants/" + docName + ".jpeg";
                }

                String directory = dossier + "uploads" + filename;
                File chemin = new File(dossier + "uploads/photo_profil_etudiants");
                if (!chemin.exists()) {
                    chemin.mkdirs();
                }
                FileOutputStream out = new FileOutputStream(directory);
                out.write(imageByte);
                out.close();

                etudiant.setPhoto(directory);
            }
            // end upload

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
        }

        // bloc to add document par Etudiant
        for (DocumentParEtudiant d : inscriptionPOJO.getDocuments()) {
            // storageService.save(d.getFile());
            // This will decode the String which is encoded by using Base64 class
            String docName = d.getDocument().getLibelle() + etudiant.getNom() + etudiant.getMatricule();
            String resp = d.getFile();
            resp = resp.replace("data:image/jpeg;base64,", "");
            byte[] imageByte = Base64.getMimeDecoder().decode(resp.trim().split(",")[1]);
            String filename = "/pdf/" + docName + ".pdf";

            String dossier = System.getProperty("user.home") + "/ecole221files/";

            if (d.getFileType().equals("image/png")) {
                filename = "/image/" + docName + ".png";
            } else if (d.getFileType().equals("image/jpg")) {
                filename = "/image/" + docName + ".jpg";
            } else if (d.getFileType().equals("image/jpeg")) {
                filename = "/image/" + docName + ".jpeg";
            } else if (d.getFileType().equals("application/pdf")) {
                filename = "/pdf/" + docName + ".pdf";
            }

            String directory = dossier + "/uploads" + filename;
            File chemin = new File(dossier + "uploads");
            if (!chemin.exists()) {
                chemin.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(directory);
            out.write(imageByte);
            out.close();

            d.setUrl(directory);
            d.setEtudiant(etudiant);
            inscriptionService.addDocumentParEtudiant(d);
        }
        // end bloc to add document par etudiant

        // bloc to add inscription
        Inscription inscription = inscriptionPOJO.getInscription();
        inscription.setAnneeScolaire(anneeScolaire);
        inscription.setArchive(false);
        inscription.setDate(Timestamp.valueOf(LocalDateTime.now()));
        inscription.setEtudiant(etudiant);
        inscription.setSousClasse(inscriptionPOJO.getSousClasse());

        inscription = inscriptionService.addInscription(inscription);
        // end bloc to add inscription

        // set default note if classe affected to a referentiel
        setDefaultNoteForNewInscription(inscription);

        return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<NoteProgrammeModule>> setDefaultNoteForNewInscription(Inscription inscription) throws InterruptedException {
        List<NoteProgrammeModule> noteProgrammeModules = new ArrayList<>();
        List<ClasseSousClasse> classeSousClasses = parametrageClasseService
                .findAllClasseSousClasseBySousClasse(inscription.getSousClasse());
        Classe classe = null;
        if (classeSousClasses.size() > 0) {
            classe = classeSousClasses.get(0).getClasse();
            AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireEnCours();
            ClasseReferentiel classeReferentiel = parametrageClasseService.findClasseReferentielByClasseAndAnneeScolaire(classe, anneeScolaire);
            if (classeReferentiel != null) {
                List<ProgrammeModule> programmeModules = parametrageReferentielService
                        .findAllProgrammeModuleByReferentiel(classeReferentiel.getReferentiel());
                if (programmeModules.size() > 0) {
                    noteProgrammeModules = noteService.setDefaultNote(programmeModules, inscription);
                    log.info("note modules size --> " + noteProgrammeModules.size());
                    noteService
                            .addListProgrammeUeInscriptionByReferentielAndInscription(classeReferentiel.getReferentiel(), inscription);
                }
            }
        }

        return CompletableFuture.completedFuture(noteProgrammeModules);
    }

    @GetMapping("inscription/annee/{idAnneeScolaire}")
    public ResponseEntity<?> getAllInscription(@PathVariable Long idAnneeScolaire) {
        if (idAnneeScolaire == null) throw new BadRequestException("id annee scolaire required");

        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireById(idAnneeScolaire);

        if (anneeScolaire == null) throw new BadRequestException("bad id");
        
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
