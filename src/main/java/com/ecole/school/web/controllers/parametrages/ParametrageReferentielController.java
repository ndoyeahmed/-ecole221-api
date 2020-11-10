package com.ecole.school.web.controllers.parametrages;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import com.ecole.school.models.Module;
import com.ecole.school.models.Niveau;
import com.ecole.school.models.ProgrammeModule;
import com.ecole.school.models.ProgrammeUE;
import com.ecole.school.models.Referentiel;
import com.ecole.school.models.Semestre;
import com.ecole.school.models.Specialite;
import com.ecole.school.services.parametrages.ParametrageModuleUEService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/parametrage-referentiel/")
public class ParametrageReferentielController {
    private ParametrageReferentielService parametrageReferentielService;
    private Utils utils;
    private ParametrageSpecialiteService parametrageSpecialiteService;
    private ParametrageModuleUEService parametrageModuleUEService;

    @Autowired
    public ParametrageReferentielController(ParametrageReferentielService parametrageReferentielService, Utils utils,
            ParametrageSpecialiteService parametrageSpecialiteService,
            ParametrageModuleUEService parametrageModuleUEService) {
        this.utils = utils;
        this.parametrageReferentielService = parametrageReferentielService;
        this.parametrageSpecialiteService = parametrageSpecialiteService;
        this.parametrageModuleUEService = parametrageModuleUEService;
    }

    // ----------------- REFERENTIEL ENDPOINTS
    @PostMapping("referentiel")
    public ResponseEntity<?> addReferentiel(@RequestBody Referentiel referentiel) {
        if (referentiel == null)
            throw new BadRequestException("body is required");
        if (referentiel.getNiveau() == null || referentiel.getNiveau().getId() == null)
            throw new BadRequestException("niveau required");
        if (referentiel.getSpecialite() == null || referentiel.getSpecialite().getId() == null)
            throw new BadRequestException("specialite required");

        referentiel.setArchive(false);
        referentiel.setDate(new Date());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parametrageReferentielService.addReferentiel(referentiel));
    }

    @GetMapping("referentiel")
    public ResponseEntity<?> getAllReferentiel() {
        return ResponseEntity.ok(parametrageReferentielService.findAllReferentiel());
    }

    @GetMapping("referentiel/niveau/{niveauId}")
    public ResponseEntity<?> getAllReferentielByNiveau(@PathVariable Long niveauId) {
        if (niveauId == null)
            throw new BadRequestException("id niveau required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(niveauId);
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageReferentielService.findAllReferentielByNiveau(niveau));
    }

    @GetMapping("referentiel/specialite/{specialiteId}")
    public ResponseEntity<?> getAllReferentielBySpecialite(@PathVariable Long specialiteId) {
        if (specialiteId == null)
            throw new BadRequestException("id niveau required");

        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(specialiteId);
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageReferentielService.findAllReferentielBySpecialite(specialite));
    }

    @GetMapping("referentiel/niveau-specialite/{niveauId}/{specialiteId}")
    public ResponseEntity<?> getAllReferentielByNiveauAndSpecialite(@PathVariable Long niveauId,
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

        return ResponseEntity
                .ok(parametrageReferentielService.findAllReferentielByNiveauAndSpecialite(niveau, specialite));
    }

    @DeleteMapping("referentiel/{id}")
    public ResponseEntity<?> archiveReferentiel(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Referentiel referentiel = parametrageReferentielService.findReferentielById(id);
        if (referentiel == null)
            throw new EntityNotFoundException("entity not found");

        referentiel.setArchive(true);
        return ResponseEntity.ok(parametrageReferentielService.addReferentiel(referentiel));
    }

    @PutMapping("referentiel/niveau/specialite/annee")
    public ResponseEntity<?> getReferentielByNiveauAndSpecialiteAndAnnee(@RequestBody Map<String, String> body) {
        if (body == null)
            throw new BadRequestException("body required");
        if (body.get("niveauId") == null)
            throw new BadRequestException("niveauId required");
        if (body.get("specialiteId") == null)
            throw new BadRequestException("specialiteId required");
        if (body.get("annee") == null)
            throw new BadRequestException("annee required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(Long.valueOf(body.get("niveauId")));
        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(Long.valueOf(body.get("specialiteId")));
        if (niveau == null)
            throw new EntityNotFoundException("entity not found");
        if (specialite == null)
            throw new EntityNotFoundException("entity not found");

        int annee = Integer.parseInt(body.get("annee"));

        return ResponseEntity.ok(parametrageReferentielService.findReferentielByNiveauAndSpecialiteAndAnnee(niveau, specialite, annee));
    }

    // ----------------- PROGRAMME UE ENDPOINTS
    @PostMapping("programme-ue")
    public ResponseEntity<?> addProgrammeUE(@RequestBody ProgrammeUE programmeUE)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (programmeUE == null)
            throw new BadRequestException("body is required");
        if (programmeUE.getReferentiel() == null || programmeUE.getReferentiel().getId() == null)
            throw new BadRequestException("referentiel required");
        if (programmeUE.getSemestre() == null || programmeUE.getSemestre().getId() == null)
            throw new BadRequestException("semestre required");
        if (programmeUE.getUe() == null || programmeUE.getUe().getId() == null)
            throw new BadRequestException("ue required");

        programmeUE.setCode(utils.generateUniqueId());
        programmeUE.setArchive(false);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parametrageReferentielService.addProgrammeUE(programmeUE));
    }

    @GetMapping("programme-ue")
    public ResponseEntity<?> getAllProgrammeUE() {
        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeUE());
    }

    @GetMapping("programme-ue/referentiel/{referentielId}")
    public ResponseEntity<?> getAllProgrammeUEByReferentiel(@PathVariable Long referentielId) {
        if (referentielId == null)
            throw new BadRequestException("id referentiel required");

        Referentiel referentiel = parametrageReferentielService.findReferentielById(referentielId);
        if (referentiel == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeUEByReferentiel(referentiel));
    }

    @GetMapping("programme-ue/semestre/{semestreId}")
    public ResponseEntity<?> getAllProgrammeUEBySemestre(@PathVariable Long semestreId) {
        if (semestreId == null)
            throw new BadRequestException("id semestre required");

        Semestre semestre = parametrageSpecialiteService.findSemestreById(semestreId);
        if (semestre == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeUEBySemestre(semestre));
    }

    @GetMapping("programme-ue/referentiel/semestre/{referentielId}/{semestreId}")
    public ResponseEntity<?> getAllProgrammeUEByReferentielAndSemestre(@PathVariable Long referentielId,
            @PathVariable Long semestreId) {
        if (referentielId == null)
            throw new BadRequestException("id referentiel required");

        Referentiel referentiel = parametrageReferentielService.findReferentielById(referentielId);
        if (referentiel == null)
            throw new EntityNotFoundException("entity not found");

        if (semestreId == null)
            throw new BadRequestException("id semestre required");

        Semestre semestre = parametrageSpecialiteService.findSemestreById(semestreId);
        if (semestre == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity
                .ok(parametrageReferentielService.findAllProgrammeUEByReferentielAndSemestre(referentiel, semestre));
    }

    @DeleteMapping("programme-ue/{id}")
    public ResponseEntity<?> archiveProgrammeUE(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        ProgrammeUE programmeUE = parametrageReferentielService.findProgrammeUEById(id);
        if (programmeUE == null)
            throw new EntityNotFoundException("entity not found");

        programmeUE.setArchive(true);
        return ResponseEntity.ok(parametrageReferentielService.addProgrammeUE(programmeUE));
    }

    // ----------------- PROGRAMME MODULE ENDPOINTS
    @PostMapping("programme-module")
    public ResponseEntity<?> addProgrammeModule(@RequestBody ProgrammeModule programmeModule)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (programmeModule == null)
            throw new BadRequestException("body is required");
        if (programmeModule.getModule() == null || programmeModule.getModule().getId() == null)
            throw new BadRequestException("module required");
        if (programmeModule.getProgrammeUE() == null || programmeModule.getProgrammeUE().getId() == null)
            throw new BadRequestException("programmeUE required");

        programmeModule.setCode(utils.generateUniqueId());
        programmeModule.setArchive(false);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parametrageReferentielService.addProgrammeModule(programmeModule));
    }

    @GetMapping("programme-module")
    public ResponseEntity<?> getAllProgrammeModule() {
        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeModule());
    }

    @GetMapping("programme-module/module/{moduleId}")
    public ResponseEntity<?> getAllProgrammeModuleByModule(@PathVariable Long moduleId) {
        if (moduleId == null)
            throw new BadRequestException("id module required");

        Module module = parametrageModuleUEService.findModuleById(moduleId);
        if (module == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeModuleByModule(module));
    }

    @GetMapping("programme-module/programme-ue/{programmeUEId}")
    public ResponseEntity<?> getAllProgrammeModuleByProgrammeUE(@PathVariable Long programmeUEId) {
        if (programmeUEId == null)
            throw new BadRequestException("id programmeUE required");

        ProgrammeUE programmeUE = parametrageReferentielService.findProgrammeUEById(programmeUEId);
        if (programmeUE == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeModuleByProgrammeUE(programmeUE));
    }

    @GetMapping("programme-module/module/programme-ue/{moduleId}/{programmeUEId}")
    public ResponseEntity<?> getAllProgrammeModuleByModuleAndProgrammeUE(@PathVariable Long moduleId,
            @PathVariable Long programmeUEId) {
        if (moduleId == null)
            throw new BadRequestException("id module required");

        Module module = parametrageModuleUEService.findModuleById(moduleId);
        if (module == null)
            throw new EntityNotFoundException("entity not found");

        if (programmeUEId == null)
            throw new BadRequestException("id programmeUE required");

        ProgrammeUE programmeUE = parametrageReferentielService.findProgrammeUEById(programmeUEId);
        if (programmeUE == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity
                .ok(parametrageReferentielService.findAllProgrammeModuleByModuleAndProgrammeUE(module, programmeUE));
    }

    @DeleteMapping("programme-module/{id}")
    public ResponseEntity<?> archiveProgrammeModule(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        ProgrammeModule programmeModule = parametrageReferentielService.finProgrammeModuleById(id);
        if (programmeModule == null)
            throw new EntityNotFoundException("entity not found");

        programmeModule.setArchive(true);
        return ResponseEntity.ok(parametrageReferentielService.addProgrammeModule(programmeModule));
    }
}
