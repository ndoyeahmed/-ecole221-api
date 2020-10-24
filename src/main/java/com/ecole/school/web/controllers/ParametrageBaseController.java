package com.ecole.school.web.controllers;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.pojos.AnneeScolairePOJO;
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
}
