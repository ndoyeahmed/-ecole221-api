package com.ecole.school.web.controllers.parametrages;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ecole.school.models.DocumentParNiveau;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.NiveauSpecialite;
import com.ecole.school.models.Semestre;
import com.ecole.school.models.SemestreNiveau;
import com.ecole.school.models.Specialite;
import com.ecole.school.services.parametrages.*;
import com.ecole.school.services.utils.Utils;
import com.ecole.school.web.POJO.NiveauPOJO;
import com.ecole.school.web.POJO.SpecialitePOJO;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.EntityNotFoundException;
import com.ecole.school.web.exceptions.InternalServerErrorException;

import lombok.extern.java.Log;
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

@Log
@RestController
@CrossOrigin
@RequestMapping("/api/parametrage-specialite/")
public class ParametrageSpecialiteController {
    private ParametrageSpecialiteService parametrageSpecialiteService;
    private Utils utils;

    @Autowired
    public ParametrageSpecialiteController(ParametrageSpecialiteService parametrageSpecialiteService, Utils utils) {
        this.parametrageSpecialiteService = parametrageSpecialiteService;
        this.utils = utils;
    }

    // ----------------- SEMESTRE ENDPOINTS
    @PostMapping("semestre")
    public ResponseEntity<?> addSemestre(@RequestBody Semestre semestre) {
        if (semestre == null)
            throw new BadRequestException("body is required");
        if (semestre.getLibelle() == null || semestre.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        semestre.setArchive(false);
        semestre.setEtat(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageSpecialiteService.addSemestre(semestre));
    }

    @GetMapping("semestre")
    public ResponseEntity<?> getAllSemestre() {
        return ResponseEntity.ok(parametrageSpecialiteService.findAllSemestre());
    }

    @DeleteMapping("semestre/{id}")
    public ResponseEntity<?> archiveSemestre(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Semestre semestre = parametrageSpecialiteService.findSemestreById(id);
        if (semestre == null)
            throw new EntityNotFoundException("entity not found");

        semestre.setArchive(true);
        return ResponseEntity.ok(parametrageSpecialiteService.addSemestre(semestre));
    }

    @PutMapping("semestre/{id}")
    public ResponseEntity<?> updateSemestre(@PathVariable Long id, @RequestBody Semestre semestre) {
        if (id == null)
            throw new BadRequestException("id required");
        if (semestre == null)
            throw new BadRequestException("body is required");
        if (semestre.getLibelle() == null || semestre.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        Semestre semestre2 = parametrageSpecialiteService.findSemestreById(id);
        if (semestre2 == null)
            throw new EntityNotFoundException("entity not found");

        semestre2.setEtat(semestre.isEtat());
        semestre2.setLibelle(semestre.getLibelle());

        return ResponseEntity.ok(parametrageSpecialiteService.addSemestre(semestre2));
    }

    @PutMapping("semestre/etat/{id}")
    public ResponseEntity<?> updateSemestreStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Semestre semestre = parametrageSpecialiteService.findSemestreById(id);
        if (semestre == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        semestre.setEtat(status);

        return ResponseEntity.ok(parametrageSpecialiteService.addSemestre(semestre));
    }

    // ----------------- NIVEAU ENDPOINTS
    @PostMapping("niveau")
    public ResponseEntity<?> addNiveau(@RequestBody NiveauPOJO niveauPOJO) {
        if (niveauPOJO == null) throw new BadRequestException("bodye required");

        Niveau niveau = niveauPOJO.getNiveau();
        List<DocumentParNiveau> documentParNiveaus = niveauPOJO.getDocumentParNiveaus();
        List<SemestreNiveau> semestreNiveaus = niveauPOJO.getSemestreNiveaus();
        if (niveau == null)
            throw new BadRequestException("body is required");
        if (niveau.getLibelle() == null || niveau.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");
        if (niveau.getCycle() == null)
            throw new BadRequestException("cycle required");
        if (niveau.getParcours() == null)
            throw new BadRequestException("parcours required");
        if (semestreNiveaus == null)
            throw new BadRequestException("body is required");
        if (semestreNiveaus.isEmpty())
            throw new BadRequestException("body is required");
        if (documentParNiveaus == null)
            throw new BadRequestException("body is required");
        if (documentParNiveaus.isEmpty())
            throw new BadRequestException("body is required");

        niveau.setArchive(false);
        niveau.setEtat(true);

        parametrageSpecialiteService.addNiveau(niveau);

        for (DocumentParNiveau d : documentParNiveaus) {
            d.setNiveau(niveau);
            parametrageSpecialiteService.addDocumentParNiveau(d);
        };

        for(SemestreNiveau d : semestreNiveaus) {
            d.setNiveau(niveau);
            parametrageSpecialiteService.addSemestreNiveau(d);
        };

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("response", true));
    }

    @GetMapping("niveau")
    public ResponseEntity<?> getAllNiveau() {
        return ResponseEntity.ok(parametrageSpecialiteService.findAllNiveau());
    }

    @DeleteMapping("niveau/{id}")
    public ResponseEntity<?> archiveNiveau(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Niveau niveau = parametrageSpecialiteService.findNiveauById(id);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        List<DocumentParNiveau> documentParNiveau = parametrageSpecialiteService
                .findAllDocumentParNiveauByNiveau(niveau);
        if (!documentParNiveau.isEmpty()) {
            documentParNiveau.parallelStream().forEach(x -> {
                x.setArchive(true);
                parametrageSpecialiteService.addDocumentParNiveau(x);
            });
        }

        List<SemestreNiveau> semestreParNiveaus = parametrageSpecialiteService.findAllSemestreNiveauByNiveau(niveau);
        if (!semestreParNiveaus.isEmpty()) {
            semestreParNiveaus.parallelStream().forEach(x -> {
                x.setArchive(true);
                parametrageSpecialiteService.addSemestreNiveau(x);
            });
        }

        niveau.setArchive(true);
        return ResponseEntity.ok(parametrageSpecialiteService.addNiveau(niveau));
    }

    @PutMapping("niveau/etat/{id}")
    public ResponseEntity<?> updateNiveauStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(id);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        niveau.setEtat(status);

        return ResponseEntity.ok(parametrageSpecialiteService.addNiveau(niveau));
    }

    @PutMapping("niveau/{id}")
    public ResponseEntity<?> updateNiveau(@PathVariable Long id, @RequestBody Niveau niveauPOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (niveauPOJO == null)
            throw new BadRequestException("body is required");
        if (niveauPOJO.getLibelle() == null || niveauPOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");
        if (niveauPOJO.getCycle() == null || niveauPOJO.getCycle().getId() == null)
            throw new BadRequestException("cycle required");
        if (niveauPOJO.getParcours() == null || niveauPOJO.getParcours().getId() == null)
            throw new BadRequestException("parcours required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(id);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        niveau.setEtat(niveauPOJO.isEtat());
        niveau.setLibelle(niveauPOJO.getLibelle());
        niveau.setNiveau(niveauPOJO.getNiveau());
        niveau.setCycle(niveauPOJO.getCycle());
        niveau.setParcours(niveauPOJO.getParcours());

        return ResponseEntity.ok(parametrageSpecialiteService.addNiveau(niveau));
    }

    // ----------------- DOCUMENT PAR NIVEAU ENDPOINTS
    @PostMapping("document-par-niveau")
    public ResponseEntity<?> addDocumentParNiveau(@RequestBody List<DocumentParNiveau> documentParNiveaus) {
        if (documentParNiveaus == null)
            throw new BadRequestException("body is required");
        if (documentParNiveaus.isEmpty())
            throw new BadRequestException("body is required");

        documentParNiveaus.parallelStream().forEach(d -> {
            parametrageSpecialiteService.addDocumentParNiveau(d);
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(documentParNiveaus);
    }

    @GetMapping("document-par-niveau/niveau/{id}")
    public ResponseEntity<?> getAllDocumentParNiveauByNiveau(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Niveau niveau = parametrageSpecialiteService.findNiveauById(id);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageSpecialiteService.findAllDocumentParNiveauByNiveau(niveau));
    }

    @PutMapping("document-par-niveau/niveau/fournir/{niveauId}")
    public ResponseEntity<?> getAllDocumentParNiveauByNiveauAndFournir(@PathVariable Long niveauId,
            @RequestBody Map<String, String> body) {
        if (niveauId == null)
            throw new BadRequestException("niveauId required");
        if (body == null)
            throw new BadRequestException("body is required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(niveauId);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));

        return ResponseEntity
                .ok(parametrageSpecialiteService.findAllDocumentParNiveauByNiveauAndFournir(niveau, status));
    }

    @DeleteMapping("document-par-niveau/{id}")
    public ResponseEntity<?> archiveDocumentParNiveau(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        DocumentParNiveau documentParNiveau = parametrageSpecialiteService.findDocumentParNiveauById(id);
        if (documentParNiveau == null)
            throw new EntityNotFoundException("entity not found");

        documentParNiveau.setArchive(true);
        return ResponseEntity.ok(parametrageSpecialiteService.addDocumentParNiveau(documentParNiveau));
    }

    // ----------------- SEMESTRE NIVEAU ENDPOINTS
    @PostMapping("semestre-niveau")
    public ResponseEntity<?> addSemestreNiveau(@RequestBody List<SemestreNiveau> semestreNiveaus) {
        if (semestreNiveaus == null)
            throw new BadRequestException("body is required");
        if (semestreNiveaus.isEmpty())
            throw new BadRequestException("body is required");

        semestreNiveaus.parallelStream().forEach(d -> {
            if (parametrageSpecialiteService.findSemestreNiveauBySemestreAndNiveau(d.getSemestre(),
                    d.getNiveau()) == null) {
                parametrageSpecialiteService.addSemestreNiveau(d);
            }
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(semestreNiveaus);
    }

    @GetMapping("semestre-niveau/niveau/{id}")
    public ResponseEntity<?> getAllSemestreNiveauByNiveau(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Niveau niveau = parametrageSpecialiteService.findNiveauById(id);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageSpecialiteService.findAllSemestreNiveauByNiveau(niveau));
    }

    @PutMapping("semestre-niveau/encours/{id}")
    public ResponseEntity<?> updateSemestreNiveauEnCours(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        SemestreNiveau semestreNiveau = parametrageSpecialiteService.findSemestreNiveauById(id);
        if (semestreNiveau == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));

        return ResponseEntity.ok(parametrageSpecialiteService.updateSemestreNiveauEnCours(semestreNiveau, status,
                semestreNiveau.getNiveau()));
    }

    @DeleteMapping("semestre-niveau/{id}")
    public ResponseEntity<?> archiveSemestreNiveau(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        SemestreNiveau semestreNiveau = parametrageSpecialiteService.findSemestreNiveauById(id);
        if (semestreNiveau == null)
            throw new EntityNotFoundException("entity not found");

        semestreNiveau.setArchive(true);
        return ResponseEntity.ok(parametrageSpecialiteService.addSemestreNiveau(semestreNiveau));
    }

    @GetMapping("semestre-niveau/encours/niveau/{niveau}")
    public ResponseEntity<?> getSemestreNiveauEncoursByNiveau(@PathVariable Long niveau) {
        try {
            if (niveau == null) throw new BadRequestException("niveau required");

            Niveau niveau1 = parametrageSpecialiteService.findNiveauById(niveau);
            if (niveau1 == null) throw new BadRequestException("niveau not found");

            return ResponseEntity.ok(parametrageSpecialiteService.findSemestreNiveauEncoursByNiveau(niveau1));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    // ----------------- SPECIALITE ENDPOINTS
    @PostMapping("specialite")
    public ResponseEntity<?> addSpecialite(@RequestBody SpecialitePOJO specialitePOJO)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InternalServerErrorException {
        if (specialitePOJO == null || specialitePOJO.getSpecialite() == null
                || specialitePOJO.getNiveauSpecialites() == null)
            throw new BadRequestException("body is required");
        if (specialitePOJO.getSpecialite().getLibelle() == null
                || specialitePOJO.getSpecialite().getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");
        if (specialitePOJO.getSpecialite().getMention() == null)
            throw new BadRequestException("mention required");

        Specialite specialite = specialitePOJO.getSpecialite();
        specialite.setNum(utils.generateUniqueId());
        specialite.setArchive(false);
        specialite.setEtat(true);

        parametrageSpecialiteService.addSpecialite(specialite);

        List<NiveauSpecialite> niveauSpecialites = specialitePOJO.getNiveauSpecialites();
        for (NiveauSpecialite d : niveauSpecialites) {
            d.setSpecialite(specialite);
            parametrageSpecialiteService.addNiveauSpecialite(d);
            /*
             * if (parametrageSpecialiteService.findNiveauSpecialiteByNiveauAndSpecialite(d.
             * getNiveau(), specialite) == null) {
             * 
             * }
             */
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("response", true));
    }

    @GetMapping("specialite")
    public ResponseEntity<?> getAllSpecialite() {
        return ResponseEntity.ok(parametrageSpecialiteService.findAllSpecialite());
    }

    @DeleteMapping("specialite/{id}")
    public ResponseEntity<?> archiveSpecialite(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(id);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        List<NiveauSpecialite> niveauSpecialites = parametrageSpecialiteService
                .findAllNiveauSpecialiteBySpecialite(specialite);
        if (!niveauSpecialites.isEmpty()) {
            niveauSpecialites.parallelStream().forEach(x -> {
                x.setArchive(true);
                parametrageSpecialiteService.addNiveauSpecialite(x);
            });
        }
        specialite.setArchive(true);
        return ResponseEntity.ok(parametrageSpecialiteService.addSpecialite(specialite));
    }

    @PutMapping("specialite/etat/{id}")
    public ResponseEntity<?> updateSpecialiteStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(id);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        specialite.setEtat(status);

        return ResponseEntity.ok(parametrageSpecialiteService.addSpecialite(specialite));
    }

    @PutMapping("specialite/{id}")
    public ResponseEntity<?> updateSpecialite(@PathVariable Long id, @RequestBody Specialite specialitePOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (specialitePOJO == null)
            throw new BadRequestException("body is required");
        if (specialitePOJO.getLibelle() == null || specialitePOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");
        if (specialitePOJO.getMention() == null || specialitePOJO.getMention().getId() == null)
            throw new BadRequestException("cycle required");

        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(id);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        specialite.setEtat(specialitePOJO.isEtat());
        specialite.setLibelle(specialitePOJO.getLibelle());
        specialite.setMention(specialitePOJO.getMention());

        return ResponseEntity.ok(parametrageSpecialiteService.addSpecialite(specialite));
    }

    // ----------------- NIVEAU SPECIALITE ENDPOINTS
    @PostMapping("niveau-specialite")
    public ResponseEntity<?> addNiveauSpecialite(@RequestBody List<NiveauSpecialite> niveauSpecialites) {
        if (niveauSpecialites == null)
            throw new BadRequestException("body is required");
        if (niveauSpecialites.isEmpty())
            throw new BadRequestException("body is required");

        niveauSpecialites.parallelStream().forEach(d -> {
            if (parametrageSpecialiteService.findNiveauSpecialiteByNiveauAndSpecialite(d.getNiveau(),
                    d.getSpecialite()) == null) {
                parametrageSpecialiteService.addNiveauSpecialite(d);
            }
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(niveauSpecialites);
    }

    @GetMapping("niveau-specialite/specialite/{id}")
    public ResponseEntity<?> getAllNiveauSpecialiteBySpecialite(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(id);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageSpecialiteService.findAllNiveauSpecialiteBySpecialite(specialite));
    }

    @GetMapping("niveau-specialite/niveau/{id}")
    public ResponseEntity<?> getAllNiveauSpecialiteByNiveau(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Niveau niveau = parametrageSpecialiteService.findNiveauById(id);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageSpecialiteService.findAllNiveauSpecialiteByNiveau(niveau));
    }

    @DeleteMapping("niveau-specialite/{id}")
    public ResponseEntity<?> archiveNiveauSpecialite(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        NiveauSpecialite niveauSpecialite = parametrageSpecialiteService.findNiveauSpecialiteById(id);
        if (niveauSpecialite == null)
            throw new EntityNotFoundException("entity not found");

        niveauSpecialite.setArchive(true);
        return ResponseEntity.ok(parametrageSpecialiteService.addNiveauSpecialite(niveauSpecialite));
    }
}
