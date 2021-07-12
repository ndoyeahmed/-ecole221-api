package com.ecole.school.web.controllers.professeur;

import com.ecole.school.models.Contrat;
import com.ecole.school.models.Professeur;
import com.ecole.school.models.ProfesseurModule;
import com.ecole.school.services.professeur.ProfesseurService;
import com.ecole.school.services.utils.FileStorageService;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.InternalServerErrorException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Log
@RequestMapping("/api/professeur/")
public class ProfesseurController {

    private ProfesseurService professeurService;
    private FileStorageService fileStorageService;

    @Autowired
    public ProfesseurController(ProfesseurService professeurService,
                                FileStorageService fileStorageService) {
        this.professeurService = professeurService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<?> addProfesseur(@RequestBody Professeur professeur,
                                           @Param("filename") String filename,
                                           @Param("filetype") String filetype) {
        try {
            fileStorageService.base64ToFile(professeur.getCv(), filename, "professeurs/cv/");
            professeur.setCv(filename);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(professeurService.addProfesseur(professeur));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProfesseur() {
        return ResponseEntity.ok(professeurService.findAllProfesseur());
    }

    @GetMapping("{professeurId}")
    public ResponseEntity<?> getProfesseurById(@PathVariable Long professeurId) {
        if (professeurId == null) throw new BadRequestException("professeurId required");

        Professeur professeur = professeurService.findProfesseurById(professeurId);
        if (professeur == null) throw new BadRequestException("professeur not exist");

        return ResponseEntity.ok(professeur);
    }

    @PostMapping("professeur-module-list")
    public ResponseEntity<?> addProfesseurModuleList(@RequestBody List<ProfesseurModule> professeurModules) {
        try {
            if (professeurModules == null) throw new BadRequestException("professeur module list required");

            return ResponseEntity.status(HttpStatus.CREATED).body(professeurService.addProfesseurModuleList(professeurModules));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @PostMapping("contrat")
    public ResponseEntity<?> addContrat(@RequestBody Contrat contrat, @Param("filename") String filename) {
        try {
            if (contrat == null) throw new BadRequestException("contrat required");
            if (contrat.getTypeContrat() == null) throw new BadRequestException("type contrat required");
            if (contrat.getProfesseur() == null) throw new BadRequestException("professeur required");

            fileStorageService.base64ToFile(contrat.getDocument(), filename, "professeurs/contrats/");
            contrat.setDocument(filename);
            return ResponseEntity.status(HttpStatus.CREATED).body(professeurService.addContrat(contrat));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @GetMapping("contrat/professeur/{professeurId}")
    public ResponseEntity<?> getContratEncoursByProfesseurId(@PathVariable Long professeurId) {
        try {
            if (professeurId == null) throw new BadRequestException("professeurId required");

            Professeur professeur = professeurService.findProfesseurById(professeurId);
            if (professeur == null) throw new BadRequestException("professeur not exist");

            return ResponseEntity.ok(professeurService.findContratEncoursByProfesseur(professeur));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @GetMapping("type-contrat")
    public ResponseEntity<?> getAllTypeContrat() {
        try {
            return ResponseEntity.ok(professeurService.findAllTypeContrat());
        } catch (Exception ex) {
            log.severe(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GetMapping("professeur-module/professeur/{professeurId}")
    public ResponseEntity<?> getListProfesseurModuleByProfesseur(@PathVariable Long professeurId) {
        try {
            if (professeurId == null) throw new BadRequestException("professeur id required");

            Professeur professeur = professeurService.findProfesseurById(professeurId);
            if (professeur == null) throw new BadRequestException("professeur not exist");

            return ResponseEntity.ok(professeurService.findAllProfesseurModuleByProfesseur(professeur));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @DeleteMapping("{professeurId}")
    public ResponseEntity<?> archiveProfesseur(@PathVariable Long professeurId) {
        try {
            if (professeurId == null) throw new BadRequestException("professeurId required");

            Professeur professeur = professeurService.findProfesseurById(professeurId);
            if (professeur == null) throw new BadRequestException("professeur not exist");

            professeur.setArchive(true);
            return ResponseEntity.ok(professeurService.addProfesseur(professeur));
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal server error");
        }
    }
}
