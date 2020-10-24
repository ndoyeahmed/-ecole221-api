package com.ecole.school.web.controllers;

import java.util.Map;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Domaine;
import com.ecole.school.pojos.AnneeScolairePOJO;
import com.ecole.school.pojos.DomainePOJO;
import com.ecole.school.services.ParametrageBaseService;
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

    @PostMapping("annee-scolaire")
    public ResponseEntity<?> addAnneeScolaire(@RequestBody AnneeScolairePOJO anneeScolaire) {
        if (anneeScolaire == null)
            throw new BadRequestException("body is required");
        if (anneeScolaire.getLibelle() == null || anneeScolaire.getLibelle().trim().equals("anObject"))
            throw new BadRequestException("libelle required");

        AnneeScolaire anneeScolaireModel = new AnneeScolaire();
        anneeScolaireModel.setLibelle(anneeScolaire.getLibelle());
        anneeScolaireModel.setEnCours(false);
        anneeScolaireModel.setArchive(false);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parametrageBaseService.addAnneeScolaire(anneeScolaireModel));
    }

    @GetMapping("annee-scolaire")
    public ResponseEntity<?> getAllAnneeScolaire() {
        return ResponseEntity.ok(parametrageBaseService.findAllAnneeScolaire());
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
            @RequestBody AnneeScolairePOJO anneeScolairePOJO) {
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

    @PostMapping("domaine")
    public ResponseEntity<?> addDomaine(@RequestBody DomainePOJO domainePOJO) {
        if (domainePOJO == null)
            throw new BadRequestException("body is required");
        if (domainePOJO.getLibelle() == null || domainePOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        Domaine domaine = new Domaine();
        domaine.setLibelle(domainePOJO.getLibelle());
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
    public ResponseEntity<?> updateDomaine(@PathVariable Long id, @RequestBody DomainePOJO domainePOJO) {
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
}
