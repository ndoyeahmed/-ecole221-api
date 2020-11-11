package com.ecole.school.web.controllers.parametrages;

import java.util.List;

import com.ecole.school.models.Classe;
import com.ecole.school.models.ClasseReferentiel;
import com.ecole.school.models.ClasseSousClasse;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.Referentiel;
import com.ecole.school.models.SousClasse;
import com.ecole.school.models.Specialite;
import com.ecole.school.services.parametrages.ParametrageClasseService;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.services.parametrages.ParametrageSpecialiteService;
import com.ecole.school.services.utils.Utils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/parametrage-classe/")
public class ParametrageClasseController {
    private ParametrageClasseService parametrageClasseService;
    private ParametrageReferentielService parametrageReferentielService;
    private ParametrageSpecialiteService parametrageSpecialiteService;
    private Utils utils;

    @Autowired
    public ParametrageClasseController(ParametrageReferentielService parametrageReferentielService, Utils utils,
            ParametrageSpecialiteService parametrageSpecialiteService,
            ParametrageClasseService parametrageClasseService) {
        this.utils = utils;
        this.parametrageReferentielService = parametrageReferentielService;
        this.parametrageSpecialiteService = parametrageSpecialiteService;
        this.parametrageClasseService = parametrageClasseService;
    }

    // ----------------- CLASSE ENDPOINTS
    @PostMapping("classe")
    public ResponseEntity<?> addClasse(@RequestBody Classe classe) {
        if (classe == null)
            throw new BadRequestException("body is required");
        if (classe.getLibelle() == null || classe.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");
        if (classe.getNiveau() == null || classe.getNiveau().getId() == null)
            throw new BadRequestException("niveau required");
        if (classe.getSpecialite() == null || classe.getSpecialite().getId() == null)
            throw new BadRequestException("specialite required");
        if (classe.getHoraire() == null || classe.getHoraire().getId() == null)
            throw new BadRequestException("horaire required");

        if (parametrageClasseService.findClasseByNiveauAndSpecialiteAndHoraire(classe.getNiveau(),
                classe.getSpecialite(), classe.getHoraire()) != null)
            throw new BadRequestException("classe already exist");

        classe.setArchive(false);
        classe.setEtat(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageClasseService.addClasse(classe));
    }

    @GetMapping("classe")
    public ResponseEntity<?> getAllClasse() {
        return ResponseEntity.ok(parametrageClasseService.findAllClasse());
    }

    @GetMapping("classe/niveau/{niveauId}")
    public ResponseEntity<?> getAllClasseByNiveau(@PathVariable Long niveauId) {
        if (niveauId == null)
            throw new BadRequestException("id niveau required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(niveauId);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageClasseService.findAllClasseByNiveau(niveau));
    }

    @GetMapping("classe/specialite/{specialiteId}")
    public ResponseEntity<?> getAllClasseBySpecialite(@PathVariable Long specialiteId) {
        if (specialiteId == null)
            throw new BadRequestException("id niveau required");

        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(specialiteId);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageClasseService.findAllClasseBySpecialite(specialite));
    }

    @GetMapping("classe/niveau-specialite/{niveauId}/{specialiteId}")
    public ResponseEntity<?> getAllClasseByNiveauAndSpecialite(@PathVariable Long niveauId,
            @PathVariable Long specialiteId) {
        if (niveauId == null)
            throw new BadRequestException("id niveau required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(niveauId);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        if (specialiteId == null)
            throw new BadRequestException("id niveau required");

        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(specialiteId);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageClasseService.findAllClasseByNiveauAndSpecialite(niveau, specialite));
    }

    @DeleteMapping("classe/{id}")
    public ResponseEntity<?> archiveClasse(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Classe classe = parametrageClasseService.findClasseById(id);
        if (classe == null)
            throw new EntityNotFoundException("entity not found");

        classe.setArchive(true);
        return ResponseEntity.ok(parametrageClasseService.addClasse(classe));
    }

    // ----------------- SOUS CLASSE ENDPOINTS
    @PostMapping("sous-classe")
    public ResponseEntity<?> addSousClasse(@RequestBody SousClasse sousClasse) {
        if (sousClasse == null)
            throw new BadRequestException("body is required");
        if (sousClasse.getLibelle() == null || sousClasse.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");
        if (sousClasse.getNiveau() == null || sousClasse.getNiveau().getId() == null)
            throw new BadRequestException("niveau required");
        if (sousClasse.getSpecialite() == null || sousClasse.getSpecialite().getId() == null)
            throw new BadRequestException("specialite required");
        if (sousClasse.getHoraire() == null || sousClasse.getHoraire().getId() == null)
            throw new BadRequestException("horaire required");

        Classe classe = parametrageClasseService.findClasseByNiveauAndSpecialiteAndHoraire(sousClasse.getNiveau(),
                sousClasse.getSpecialite(), sousClasse.getHoraire());
        if (classe == null)
            throw new BadRequestException("can not bind to any existing classe");

        sousClasse.setArchive(false);
        sousClasse.setEtat(true);

        sousClasse = parametrageClasseService.addSousClasse(sousClasse);

        ClasseSousClasse classeSousClasse = new ClasseSousClasse();
        classeSousClasse.setClasse(classe);
        classeSousClasse.setSousClasse(sousClasse);
        classeSousClasse.setArchive(false);

        parametrageClasseService.addClasseSousClasse(classeSousClasse);

        return ResponseEntity.status(HttpStatus.CREATED).body(sousClasse);
    }

    @GetMapping("sous-classe")
    public ResponseEntity<?> getAllSousClasse() {
        return ResponseEntity.ok(parametrageClasseService.findAllSousClasse());
    }

    @GetMapping("sous-classe/niveau/{niveauId}")
    public ResponseEntity<?> getAllSousClasseByNiveau(@PathVariable Long niveauId) {
        if (niveauId == null)
            throw new BadRequestException("id niveau required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(niveauId);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageClasseService.findAllSousClasseByNiveau(niveau));
    }

    @GetMapping("sous-classe/specialite/{specialiteId}")
    public ResponseEntity<?> getAllSousClasseBySpecialite(@PathVariable Long specialiteId) {
        if (specialiteId == null)
            throw new BadRequestException("id niveau required");

        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(specialiteId);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageClasseService.findAllSousClasseBySpecialite(specialite));
    }

    @GetMapping("sous-classe/niveau-specialite/{niveauId}/{specialiteId}")
    public ResponseEntity<?> getAllSousClasseByNiveauAndSpecialite(@PathVariable Long niveauId,
            @PathVariable Long specialiteId) {
        if (niveauId == null)
            throw new BadRequestException("id niveau required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(niveauId);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        if (specialiteId == null)
            throw new BadRequestException("id niveau required");

        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(specialiteId);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageClasseService.findAllSousClasseByNiveauAndSpecialite(niveau, specialite));
    }

    @DeleteMapping("sous-classe/{id}")
    public ResponseEntity<?> archiveSousClasse(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        SousClasse sousClasse = parametrageClasseService.findSousClasseById(id);
        if (sousClasse == null)
            throw new EntityNotFoundException("entity not found");

        sousClasse.setArchive(true);
        return ResponseEntity.ok(parametrageClasseService.addSousClasse(sousClasse));
    }

    // ----------------- CLASSE REFERENTIEL ENDPOINTS
    @PostMapping("classe-referentiel")
    public ResponseEntity<?> addClasseReferentiel(@RequestBody List<ClasseReferentiel> classeReferentiels) {
        if (classeReferentiels == null)
            throw new BadRequestException("body is required");
        if (classeReferentiels.isEmpty())
            throw new BadRequestException("body is required");

        classeReferentiels.parallelStream().forEach(d -> {
            if (parametrageClasseService.findClasseReferentielByClasseAndReferentiel(d.getClasse(),
                    d.getReferentiel()) == null) {
                parametrageClasseService.addClasseReferentiel(d);
            }
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(classeReferentiels);
    }

    @GetMapping("classe-referentiel/classe/{classeId}/referentiel/{referentielId}")
    public ResponseEntity<?> getClasseReferentielByClasseAndReferentiel(@PathVariable Long classeId,
            @PathVariable Long referentielId) {
        if (classeId == null)
            throw new BadRequestException("classeId required");
        if (referentielId == null)
            throw new BadRequestException("referentielId required");

        Classe classe = parametrageClasseService.findClasseById(classeId);
        if (classe == null)
            throw new EntityNotFoundException("classe not found");

        Referentiel referentiel = parametrageReferentielService.findReferentielById(referentielId);
        if (referentiel == null)
            throw new EntityNotFoundException("referentiel not found");

        return ResponseEntity
                .ok(parametrageClasseService.findClasseReferentielByClasseAndReferentiel(classe, referentiel));
    }

}
