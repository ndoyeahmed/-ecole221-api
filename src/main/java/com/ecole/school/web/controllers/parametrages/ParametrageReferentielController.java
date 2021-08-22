package com.ecole.school.web.controllers.parametrages;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.ecole.school.models.*;
import com.ecole.school.models.Module;
import com.ecole.school.services.parametrages.*;
import com.ecole.school.services.utils.ExcelWriter;
import com.ecole.school.services.utils.FileStorageService;
import com.ecole.school.services.utils.Utils;
import com.ecole.school.web.POJO.RecapReferentiel;
import com.ecole.school.web.POJO.ResponseMessage;
import com.ecole.school.web.exceptions.BadRequestException;
import com.ecole.school.web.exceptions.EntityNotFoundException;

import com.ecole.school.web.exceptions.InternalServerErrorException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Log
@RequestMapping("/api/parametrage-referentiel/")
public class ParametrageReferentielController {
    private ParametrageReferentielService parametrageReferentielService;
    private Utils utils;
    private ParametrageSpecialiteService parametrageSpecialiteService;
    private ParametrageModuleUEService parametrageModuleUEService;
    private ParametrageClasseService parametrageClasseService;
    private ParametrageBaseService parametrageBaseService;
    private ExcelWriter excelWriter;

    @Autowired
    public ParametrageReferentielController(ParametrageReferentielService parametrageReferentielService, Utils utils,
            ParametrageSpecialiteService parametrageSpecialiteService, ExcelWriter excelWriter, ParametrageBaseService parametrageBaseService,
            ParametrageModuleUEService parametrageModuleUEService, ParametrageClasseService parametrageClasseService) {
        this.utils = utils;
        this.parametrageReferentielService = parametrageReferentielService;
        this.parametrageSpecialiteService = parametrageSpecialiteService;
        this.parametrageModuleUEService = parametrageModuleUEService;
        this.excelWriter = excelWriter;
        this.parametrageClasseService = parametrageClasseService;
        this.parametrageBaseService = parametrageBaseService;
    }

    // ----------------- REFERENTIEL ENDPOINTS

    @PostMapping("referentiel/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            if (excelWriter.hasExcelFormat(file)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(excelWriter.excelToRecapReferentiel(file.getInputStream()));
            } else return ResponseEntity.status(400)
                    .body("incorrect file format");

        } catch (Exception e) {
            log.severe(e.getMessage());
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

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

    private void cloneRef(Referentiel oldRef, Referentiel newRef) {
        List<ProgrammeUE> programmeUEs = parametrageReferentielService.findAllProgrammeUEByReferentiel(oldRef);
        if (!programmeUEs.isEmpty()) {
            // clone programme ue and programme module
            programmeUEs.parallelStream().forEach(x -> {
                List<ProgrammeModule> programmeModules = parametrageReferentielService
                        .findAllProgrammeModuleByProgrammeUE(x.getId());
                ProgrammeUE pu = new ProgrammeUE();
                pu.setArchive(x.isArchive());
                try {
                    pu.setCode(utils.generateUniqueId());
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                pu.setCredit(x.getCredit());
                pu.setFondamental(x.getFondamental());
                pu.setNbreHeureUE(x.getNbreHeureUE());
                pu.setUe(x.getUe());
                pu.setSemestre(x.getSemestre());
                pu.setReferentiel(newRef);
                parametrageReferentielService.addProgrammeUE(pu);
                if (!programmeModules.isEmpty()) {
                    for (ProgrammeModule pm : programmeModules) {
                        ProgrammeModule prModule = new ProgrammeModule();
                        try {
                            prModule.setCode(utils.generateUniqueId());
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        prModule.setBudget(pm.getBudget());
                        prModule.setCoef(pm.getCoef());
                        prModule.setNbreCreditModule(pm.getNbreCreditModule());
                        prModule.setTd(pm.getTd());
                        prModule.setTp(pm.getTp());
                        prModule.setTpe(pm.getTpe());
                        prModule.setVhp(pm.getVhp());
                        prModule.setVht(pm.getVht());
                        prModule.setModule(pm.getModule());
                        prModule.setProgrammeUE(pu);
                        parametrageReferentielService.addProgrammeModule(pm);
                    }
                }
            });
        }
    }

    @PostMapping("referentiel/{id}")
    public ResponseEntity<?> cloneReferentiel(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("old id required");

        Referentiel oldRef = parametrageReferentielService.findReferentielById(id);
        if (oldRef == null)
            throw new EntityNotFoundException("old referentiel not found");

        if (body == null)
            throw new BadRequestException("body is required");
        if (body.get("niveau") == null)
            throw new BadRequestException("niveau required");
        if (body.get("specialite") == null)
            throw new BadRequestException("specialite required");
        if (body.get("annee") == null)
            throw new BadRequestException("annee required");
        if (body.get("credit") == null)
            throw new BadRequestException("credit required");

        Niveau niveau = parametrageSpecialiteService.findNiveauById(Long.valueOf(body.get("niveau")));
        if (niveau == null)
            throw new EntityNotFoundException("niveau not found");
        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(Long.valueOf(body.get("specialite")));
        if (specialite == null)
            throw new EntityNotFoundException("specialite not found");

        Referentiel referentiel = new Referentiel();
        referentiel.setNiveau(niveau);
        referentiel.setSpecialite(specialite);
        referentiel.setAnnee(Integer.parseInt(body.get("annee")));
        referentiel.setCredit(Integer.parseInt(body.get("credit")));
        referentiel.setVolumeHeureTotal(Integer.parseInt(body.get("volumeHeureTotal")));
        referentiel.setDescription(body.get("description"));
        referentiel.setArchive(false);
        referentiel.setDate(new Date());
        referentiel = parametrageReferentielService.addReferentiel(referentiel);

        cloneRef(oldRef, referentiel);

        return ResponseEntity.status(HttpStatus.CREATED).body(referentiel);
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

        List<ProgrammeUE> programmeUEs = parametrageReferentielService.findAllProgrammeUEByReferentiel(referentiel);
        if (!programmeUEs.isEmpty()) {
            programmeUEs.parallelStream().forEach(x -> {
                // archive programme ue
                x.setArchive(true);
                parametrageReferentielService.addProgrammeUE(x);

                // archive programme module
                List<ProgrammeModule> programmeModules = parametrageReferentielService
                        .findAllProgrammeModuleByProgrammeUE(x.getId());
                if (!programmeModules.isEmpty()) {
                    programmeModules.parallelStream().forEach(m -> {
                        m.setArchive(true);
                        parametrageReferentielService.addProgrammeModule(m);
                    });
                }
            });
        }

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

        return ResponseEntity.ok(
                parametrageReferentielService.findReferentielByNiveauAndSpecialiteAndAnnee(niveau, specialite, annee));
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

        programmeUE.setCode(utils.generateUECode(programmeUE.getUe().getLibelle(),
                programmeUE.getReferentiel().getNiveau().getNiveau(),
                programmeUE.getSemestre().getNumero(),
                programmeUE.getUe().getNumero()));
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
            throws IOException, NoSuchAlgorithmException {
        if (programmeModule == null)
            throw new BadRequestException("body is required");
        if (programmeModule.getModule() == null || programmeModule.getModule().getId() == null)
            throw new BadRequestException("module required");
        if (programmeModule.getProgrammeUE() == null || programmeModule.getProgrammeUE().getId() == null)
            throw new BadRequestException("programmeUE required");

        programmeModule.setCode(utils.generateModuleCode(
                programmeModule.getProgrammeUE().getCode(),
                programmeModule.getModule().getNumero()
        ));
        programmeModule.setArchive(false);

        // upload syllabus
        if (programmeModule.getSyllabus() != null && !programmeModule.getSyllabus().trim().equals("")) {
            String docName = programmeModule.getCode();
            String resp = programmeModule.getSyllabus();
            resp = resp.replace("data:application/pdf;base64,", "");
            byte[] imageByte = Base64.getMimeDecoder().decode(resp.trim().split(",")[0]);
            String filename = "/syllabus/" + programmeModule.getModule().getLibelle()
                    + "/" + docName + ".pdf";

            String dossier = System.getProperty("user.home") + "/ecole221files/";


            String directory = dossier + "uploads" + filename;
            File chemin = new File(dossier + "uploads/syllabus/"
                    + programmeModule.getModule().getLibelle());
            if (!chemin.exists()) {
                chemin.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(directory);
            out.write(imageByte);
            out.close();

            programmeModule.setSyllabus(directory);
        }
        // end upload

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parametrageReferentielService.addProgrammeModule(programmeModule));
    }

    @GetMapping("programme-module")
    public ResponseEntity<?> getAllProgrammeModule() {
        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeModule());
    }

    @PostMapping("get-recap-referentiel")
    public void getListRecapReferentiel(@RequestBody List<RecapReferentiel> recapReferentiels) {
        System.out.println(recapReferentiels.get(0).getSemestre().getLibelle());
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

        return ResponseEntity.ok(parametrageReferentielService.findAllProgrammeModuleByProgrammeUE(programmeUE.getId()));
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

    @GetMapping("programme-module/programme-ue/referentiel/{referentielId}")
    public ResponseEntity<?> getAllProgrammeModuleByReferentiel(@PathVariable Long referentielId) {
        if (referentielId == null) throw new BadRequestException("referentielId not correct");

        Referentiel referentiel = parametrageReferentielService.findReferentielById(referentielId);

        if (referentiel == null) throw new BadRequestException("referentiel not found");

        return ResponseEntity.ok(parametrageReferentielService
                .findAllProgrammeModuleByReferentiel(referentiel));
    }

    @GetMapping("programme-module/programme-ue/referentiel/{referentielId}/semestre/{semestreId}")
    public ResponseEntity<?> getAllProgrammeModuleByReferentielAndSemestre(@PathVariable Long referentielId
            , @PathVariable Long semestreId) {

        if (referentielId == null) throw new BadRequestException("referentielId not correct");
        if (semestreId == null) throw new BadRequestException("semestreId not correct");

        Referentiel referentiel = parametrageReferentielService.findReferentielById(referentielId);
        Semestre semestre = parametrageSpecialiteService.findSemestreById(semestreId);

        if (referentiel == null) throw new BadRequestException("referentiel not found");
        if (semestre == null) throw new BadRequestException("semestre not found");

        return ResponseEntity.ok(parametrageReferentielService
                .findAllProgrammeModuleByReferentielAndSemestre(referentiel, semestre));
    }

    @GetMapping("programme-module/classe/{classeId}/semestre/{semestreId}")
    public ResponseEntity<?> getListProgrammeModuleByClasseAndSemestreAndAnneeScolaire(@PathVariable Long classeId, @PathVariable Long semestreId) {
        try {
            if (classeId == null) throw new BadRequestException("classeId required");
            if (semestreId == null) throw new BadRequestException("semestreId required");

            Classe classe = parametrageClasseService.findClasseById(classeId);
            if (classe == null) throw new BadRequestException("classe not found");

            Semestre semestre = parametrageSpecialiteService.findSemestreById(semestreId);
            if (semestre == null) throw new BadRequestException("semestre not found");

            AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireEnCours();
            ClasseReferentiel classeReferentiel = parametrageClasseService.findClasseReferentielByClasseAndAnneeScolaire(classe, anneeScolaire);
            if (classeReferentiel == null) throw new BadRequestException("classe not affected to a referentiel");

            List<ProgrammeModule> programmeModules = parametrageReferentielService
                    .findAllProgrammeModuleByReferentielAndSemestre(classeReferentiel.getReferentiel(), semestre);

            return ResponseEntity.ok(programmeModules);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException("Internal Server error");
        }
    }
}
