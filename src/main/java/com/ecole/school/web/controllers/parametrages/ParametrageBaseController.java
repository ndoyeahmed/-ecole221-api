package com.ecole.school.web.controllers.parametrages;

import java.util.Map;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Cycle;
import com.ecole.school.models.Document;
import com.ecole.school.models.Domaine;
import com.ecole.school.models.Horaire;
import com.ecole.school.models.Mention;
import com.ecole.school.models.Parcours;
import com.ecole.school.services.parametrages.*;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/parametrage-base/")
public class ParametrageBaseController {
    private ParametrageBaseService parametrageBaseService;

    @Autowired
    public ParametrageBaseController(ParametrageBaseService parametrageBaseService) {
        this.parametrageBaseService = parametrageBaseService;
    }

    // ----------------- ANNEE SCOLAIRE ENDPOINTS
    @PostMapping("annee-scolaire")
    public ResponseEntity<?> addAnneeScolaire(@RequestBody AnneeScolaire anneeScolaire) {
        if (anneeScolaire == null)
            throw new BadRequestException("body is required");
        if (anneeScolaire.getLibelle() == null || anneeScolaire.getLibelle().trim().equals("anObject"))
            throw new BadRequestException("libelle required");

        anneeScolaire.setEnCours(false);
        anneeScolaire.setArchive(false);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parametrageBaseService.addAnneeScolaire(anneeScolaire));
    }

    @GetMapping("annee-scolaire")
    public ResponseEntity<?> getAllAnneeScolaire() {
        return ResponseEntity.ok(parametrageBaseService.findAllAnneeScolaire());
    }

    @GetMapping("annee-scolaire/encours")
    public ResponseEntity<?> getAnneeScolaireEncour() {
        return ResponseEntity.ok(parametrageBaseService.findAnneeScolaireEnCours());
    }

    @DeleteMapping("annee-scolaire/{id}")
    public ResponseEntity<?> archiveAnneeScolaire(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireById(id);
        if (anneeScolaire == null)
            throw new EntityNotFoundException("entity not found");

        anneeScolaire.setArchive(true);
        return ResponseEntity.ok(parametrageBaseService.addAnneeScolaire(anneeScolaire));
    }

    @PutMapping("annee-scolaire/{id}")
    public ResponseEntity<?> updateAnneeScolaire(@PathVariable Long id,
            @RequestBody AnneeScolaire anneeScolairePOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (anneeScolairePOJO == null)
            throw new BadRequestException("body is required");
        if (anneeScolairePOJO.getLibelle() == null || anneeScolairePOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireById(id);
        if (anneeScolaire == null)
            throw new EntityNotFoundException("entity not found");

        anneeScolaire.setEnCours(anneeScolairePOJO.isEnCours());
        anneeScolaire.setLibelle(anneeScolairePOJO.getLibelle());

        return ResponseEntity.ok(parametrageBaseService.addAnneeScolaire(anneeScolaire));
    }

    @PutMapping("annee-scolaire/encours/{id}")
    public ResponseEntity<?> updateAnneeScolaireEnCours(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireById(id);
        if (anneeScolaire == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));

        return ResponseEntity.ok(parametrageBaseService.updateAnneeScolaireEnCours(anneeScolaire, status));
    }

    // ----------------- DOMAINE ENDPOINTS
    @PostMapping("domaine")
    public ResponseEntity<?> addDomaine(@RequestBody Domaine domaine) {
        if (domaine == null)
            throw new BadRequestException("body is required");
        if (domaine.getLibelle() == null || domaine.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        domaine.setArchive(false);
        domaine.setEtat(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageBaseService.addDomaine(domaine));
    }

    @GetMapping("domaine")
    public ResponseEntity<?> getAllDomaine() {
        return ResponseEntity.ok(parametrageBaseService.findAllDomaine());
    }

    @DeleteMapping("domaine/{id}")
    public ResponseEntity<?> archiveDomaine(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Domaine domaine = parametrageBaseService.findDomaineById(id);
        if (domaine == null)
            throw new EntityNotFoundException("entity not found");

        domaine.setArchive(true);
        return ResponseEntity.ok(parametrageBaseService.addDomaine(domaine));
    }

    @PutMapping("domaine/{id}")
    public ResponseEntity<?> updateDomaine(@PathVariable Long id, @RequestBody Domaine domainePOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (domainePOJO == null)
            throw new BadRequestException("body is required");
        if (domainePOJO.getLibelle() == null || domainePOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        Domaine domaine = parametrageBaseService.findDomaineById(id);
        if (domaine == null)
            throw new EntityNotFoundException("entity not found");

        domaine.setEtat(domainePOJO.isEtat());
        domaine.setLibelle(domainePOJO.getLibelle());

        return ResponseEntity.ok(parametrageBaseService.addDomaine(domaine));
    }

    @PutMapping("domaine/etat/{id}")
    public ResponseEntity<?> updateDomaineStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Domaine domaine = parametrageBaseService.findDomaineById(id);
        if (domaine == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        domaine.setEtat(status);

        return ResponseEntity.ok(parametrageBaseService.addDomaine(domaine));
    }

    // ----------------- CYCLE ENDPOINTS
    @PostMapping("cycle")
    public ResponseEntity<?> addCycle(@RequestBody Cycle cycle) {
        if (cycle == null)
            throw new BadRequestException("body is required");
        if (cycle.getCycle() == null || cycle.getCycle().trim().equals(""))
            throw new BadRequestException("cycle is required");

        cycle.setArchive(false);
        cycle.setEtat(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageBaseService.addCycle(cycle));
    }

    @GetMapping("cycle")
    public ResponseEntity<?> getAllCycle() {
        return ResponseEntity.ok(parametrageBaseService.findAllCycle());
    }

    @DeleteMapping("cycle/{id}")
    public ResponseEntity<?> archiveCycle(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Cycle cycle = parametrageBaseService.findCycleById(id);
        if (cycle == null)
            throw new EntityNotFoundException("entity not found");

        cycle.setArchive(true);
        return ResponseEntity.ok(parametrageBaseService.addCycle(cycle));
    }

    @PutMapping("cycle/{id}")
    public ResponseEntity<?> updateCycle(@PathVariable Long id, @RequestBody Cycle cyclePOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (cyclePOJO == null)
            throw new BadRequestException("body is required");
        if (cyclePOJO.getCycle() == null || cyclePOJO.getCycle().trim().equals(""))
            throw new BadRequestException("cycle required");

        Cycle cycle = parametrageBaseService.findCycleById(id);
        if (cycle == null)
            throw new EntityNotFoundException("entity not found");

        cycle.setEtat(cyclePOJO.isEtat());
        cycle.setCycle(cyclePOJO.getCycle());

        return ResponseEntity.ok(parametrageBaseService.addCycle(cycle));
    }

    @PutMapping("cycle/etat/{id}")
    public ResponseEntity<?> updateCycleStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Cycle cycle = parametrageBaseService.findCycleById(id);
        if (cycle == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        cycle.setEtat(status);

        return ResponseEntity.ok(parametrageBaseService.addCycle(cycle));
    }

    // ----------------- PARCOURS ENDPOINTS
    @PostMapping("parcours")
    public ResponseEntity<?> addParcours(@RequestBody Parcours parcours) {
        if (parcours == null)
            throw new BadRequestException("body is required");
        if (parcours.getLibelle() == null || parcours.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        parcours.setArchive(false);
        parcours.setEtat(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageBaseService.addParcours(parcours));
    }

    @GetMapping("parcours")
    public ResponseEntity<?> getAllParcours() {
        return ResponseEntity.ok(parametrageBaseService.findAllParcours());
    }

    @DeleteMapping("parcours/{id}")
    public ResponseEntity<?> archiveParcours(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Parcours parcours = parametrageBaseService.findParcoursById(id);
        if (parcours == null)
            throw new EntityNotFoundException("entity not found");

        parcours.setArchive(true);
        return ResponseEntity.ok(parametrageBaseService.addParcours(parcours));
    }

    @PutMapping("parcours/{id}")
    public ResponseEntity<?> updateParcours(@PathVariable Long id, @RequestBody Parcours parcoursPOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (parcoursPOJO == null)
            throw new BadRequestException("body is required");
        if (parcoursPOJO.getLibelle() == null || parcoursPOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        Parcours parcours = parametrageBaseService.findParcoursById(id);
        if (parcours == null)
            throw new EntityNotFoundException("entity not found");

        parcours.setEtat(parcoursPOJO.isEtat());
        parcours.setLibelle(parcoursPOJO.getLibelle());

        return ResponseEntity.ok(parametrageBaseService.addParcours(parcours));
    }

    @PutMapping("parcours/etat/{id}")
    public ResponseEntity<?> updateParcoursStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Parcours parcours = parametrageBaseService.findParcoursById(id);
        if (parcours == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        parcours.setEtat(status);

        return ResponseEntity.ok(parametrageBaseService.addParcours(parcours));
    }

    // ----------------- HORAIRE ENDPOINTS
    @PostMapping("horaire")
    public ResponseEntity<?> addHoraire(@RequestBody Horaire horaire) {
        if (horaire == null)
            throw new BadRequestException("body is required");
        if (horaire.getLibelle() == null || horaire.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        horaire.setArchive(false);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageBaseService.addHoraire(horaire));
    }

    @GetMapping("horaire")
    public ResponseEntity<?> getAllHoraire() {
        return ResponseEntity.ok(parametrageBaseService.findAllHoraire());
    }

    @DeleteMapping("horaire/{id}")
    public ResponseEntity<?> archiveHoraire(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Horaire horaire = parametrageBaseService.findHoraireById(id);
        if (horaire == null)
            throw new EntityNotFoundException("entity not found");

        horaire.setArchive(true);
        return ResponseEntity.ok(parametrageBaseService.addHoraire(horaire));
    }

    @PutMapping("horaire/{id}")
    public ResponseEntity<?> updateHoraire(@PathVariable Long id, @RequestBody Horaire horairePOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (horairePOJO == null)
            throw new BadRequestException("body is required");
        if (horairePOJO.getLibelle() == null || horairePOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        Horaire horaire = parametrageBaseService.findHoraireById(id);
        if (horaire == null)
            throw new EntityNotFoundException("entity not found");

        horaire.setLibelle(horairePOJO.getLibelle());

        return ResponseEntity.ok(parametrageBaseService.addHoraire(horaire));
    }

    // ----------------- DOCUMENT ENDPOINTS
    @PostMapping("document")
    public ResponseEntity<?> addDocument(@RequestBody Document document) {
        if (document == null)
            throw new BadRequestException("body is required");
        if (document.getLibelle() == null || document.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        document.setArchive(false);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageBaseService.addDocument(document));
    }

    @GetMapping("document")
    public ResponseEntity<?> getAllDocument() {
        return ResponseEntity.ok(parametrageBaseService.findAllDocument());
    }

    @DeleteMapping("document/{id}")
    public ResponseEntity<?> archiveDocument(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Document document = parametrageBaseService.findDocumentById(id);
        if (document == null)
            throw new EntityNotFoundException("entity not found");

        document.setArchive(true);
        return ResponseEntity.ok(parametrageBaseService.addDocument(document));
    }

    @PutMapping("document/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable Long id, @RequestBody Document documentPOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (documentPOJO == null)
            throw new BadRequestException("body is required");
        if (documentPOJO.getLibelle() == null || documentPOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        Document document = parametrageBaseService.findDocumentById(id);
        if (document == null)
            throw new EntityNotFoundException("entity not found");

        document.setLibelle(documentPOJO.getLibelle());

        return ResponseEntity.ok(parametrageBaseService.addDocument(document));
    }

    // ----------------- MENTION ENDPOINTS
    @PostMapping("mention")
    public ResponseEntity<?> addMention(@RequestBody Mention mention) {
        if (mention == null)
            throw new BadRequestException("body is required");
        if (mention.getLibelle() == null || mention.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");
        if (mention.getDomaine() == null)
            throw new BadRequestException("domaine required");

        mention.setEtat(true);
        mention.setArchive(false);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageBaseService.addMention(mention));
    }

    @GetMapping("mention")
    public ResponseEntity<?> getAllMention() {
        return ResponseEntity.ok(parametrageBaseService.findAllMention());
    }

    @DeleteMapping("mention/{id}")
    public ResponseEntity<?> archiveMention(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Mention mention = parametrageBaseService.findMentionById(id);
        if (mention == null)
            throw new EntityNotFoundException("entity not found");

        mention.setArchive(true);
        return ResponseEntity.ok(parametrageBaseService.addMention(mention));
    }

    @PutMapping("mention/{id}")
    public ResponseEntity<?> updateMention(@PathVariable Long id, @RequestBody Mention mentionPOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (mentionPOJO == null)
            throw new BadRequestException("body is required");
        if (mentionPOJO.getLibelle() == null || mentionPOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");
        if (mentionPOJO.getDomaine() == null)
            throw new BadRequestException("domaine required");

        Mention mention = parametrageBaseService.findMentionById(id);
        if (mention == null)
            throw new EntityNotFoundException("entity not found");


        mention.setLibelle(mentionPOJO.getLibelle());
        mention.setDomaine(mentionPOJO.getDomaine());

        return ResponseEntity.ok(parametrageBaseService.addMention(mention));
    }

    @PutMapping("mention/etat/{id}")
    public ResponseEntity<?> updateMentionStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Mention mention = parametrageBaseService.findMentionById(id);
        if (mention == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        mention.setEtat(status);

        return ResponseEntity.ok(parametrageBaseService.addMention(mention));
    }
}
