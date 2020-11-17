package com.ecole.school.web.controllers.parametrages;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.ecole.school.models.MentionModule;
import com.ecole.school.models.MentionUE;
import com.ecole.school.models.Module;
import com.ecole.school.models.Specialite;
import com.ecole.school.models.UE;
import com.ecole.school.services.parametrages.ParametrageModuleUEService;
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
@RequestMapping("/api/parametrage-module-ue/")
public class ParametrageModuleUEController {
    private ParametrageModuleUEService parametrageModuleUEService;
    private ParametrageSpecialiteService parametrageSpecialiteService;
    private Utils utils;

    @Autowired
    public ParametrageModuleUEController(ParametrageModuleUEService parametrageModuleUEService, Utils utils, ParametrageSpecialiteService parametrageSpecialiteService) {
        this.parametrageModuleUEService = parametrageModuleUEService;
        this.utils = utils;
        this.parametrageSpecialiteService = parametrageSpecialiteService;
    }

    // ----------------- MODULE ENDPOINTS
    @PostMapping("modules")
    public ResponseEntity<?> addModule(@RequestBody Module module)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (module == null)
            throw new BadRequestException("body is required");
        if (module.getLibelle() == null || module.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        module.setCode(utils.createCode(module.getLibelle()));
        module.setArchive(false);
        module.setEtat(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageModuleUEService.addModule(module));
    }

    @GetMapping("modules")
    public ResponseEntity<?> getAllModule() {
        return ResponseEntity.ok(parametrageModuleUEService.findAllModule());
    }

    @DeleteMapping("modules/{id}")
    public ResponseEntity<?> archiveModule(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Module module = parametrageModuleUEService.findModuleById(id);
        if (module == null)
            throw new EntityNotFoundException("entity not found");

        module.setArchive(true);
        return ResponseEntity.ok(parametrageModuleUEService.addModule(module));
    }

    @PutMapping("modules/etat/{id}")
    public ResponseEntity<?> updateModuleStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        Module module = parametrageModuleUEService.findModuleById(id);
        if (module == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        module.setEtat(status);

        return ResponseEntity.ok(parametrageModuleUEService.addModule(module));
    }

    @PutMapping("modules/{id}")
    public ResponseEntity<?> updateModule(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");
        if (body.get("libelle") == null || body.get("libelle").trim().equals(""))
            throw new BadRequestException("libelle is required");

        Module module = parametrageModuleUEService.findModuleById(id);
        if (module == null)
            throw new EntityNotFoundException("entity not found");

        module.setLibelle(body.get("libelle"));

        return ResponseEntity.ok(parametrageModuleUEService.addModule(module));
    }

    // ----------------- MENTION MODULE ENDPOINTS
    @PostMapping("mention-module")
    public ResponseEntity<?> addMentionModule(@RequestBody List<MentionModule> mentionModules) {
        if (mentionModules == null)
            throw new BadRequestException("body is required");
        if (mentionModules.isEmpty())
            throw new BadRequestException("body is required");

        mentionModules.parallelStream().forEach(d -> parametrageModuleUEService.addMentionModule(d));
        return ResponseEntity.status(HttpStatus.CREATED).body(mentionModules);
    }

    @GetMapping("mention-module/module/{id}")
    public ResponseEntity<?> getAllMentionModuleByModule(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        Module module = parametrageModuleUEService.findModuleById(id);
        if (module == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageModuleUEService.findAllMentionModuleByModule(module));
    }

    @DeleteMapping("mention-module/{id}")
    public ResponseEntity<?> archiveMentionModule(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        MentionModule mentionModule = parametrageModuleUEService.findMentionModuleById(id);
        if (mentionModule == null)
            throw new EntityNotFoundException("entity not found");

        mentionModule.setArchive(true);
        return ResponseEntity.ok(parametrageModuleUEService.addMentionModule(mentionModule));
    }

    @GetMapping("mention-module/mention/{specialiteId}")
    public ResponseEntity<?> getAllMentionModuleByMention(@PathVariable Long specialiteId) {
        if (specialiteId == null)
            throw new BadRequestException("specialiteId required");
        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(specialiteId);
        if (specialite == null)
            throw new EntityNotFoundException("specialite not found");

        return ResponseEntity.ok(parametrageModuleUEService.findAllMentionModuleByMention(specialite.getMention()));
    }

    // ----------------- UE ENDPOINTS
    @PostMapping("ue")
    public ResponseEntity<?> addUE(@RequestBody UE ue) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (ue == null)
            throw new BadRequestException("body is required");
        if (ue.getLibelle() == null || ue.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        ue.setCode(utils.createCode(ue.getLibelle()));
        ue.setArchive(false);
        ue.setEtat(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(parametrageModuleUEService.addUE(ue));
    }

    @GetMapping("ue")
    public ResponseEntity<?> getAllUE() {
        return ResponseEntity.ok(parametrageModuleUEService.findAllUE());
    }

    @DeleteMapping("ue/{id}")
    public ResponseEntity<?> archiveUE(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        UE ue = parametrageModuleUEService.findUEById(id);
        if (ue == null)
            throw new EntityNotFoundException("entity not found");

        ue.setArchive(true);
        return ResponseEntity.ok(parametrageModuleUEService.addUE(ue));
    }

    @PutMapping("ue/etat/{id}")
    public ResponseEntity<?> updateUEStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");

        UE ue = parametrageModuleUEService.findUEById(id);
        if (ue == null)
            throw new EntityNotFoundException("entity not found");

        boolean status = Boolean.parseBoolean(body.get("status"));
        ue.setEtat(status);

        return ResponseEntity.ok(parametrageModuleUEService.addUE(ue));
    }

    @PutMapping("ue/{id}")
    public ResponseEntity<?> updateUE(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (id == null)
            throw new BadRequestException("id required");
        if (body == null)
            throw new BadRequestException("body is required");
        if (body.get("libelle") == null || body.get("libelle").trim().equals(""))
            throw new BadRequestException("libelle is required");

        UE ue = parametrageModuleUEService.findUEById(id);
        if (ue == null)
            throw new EntityNotFoundException("entity not found");

        ue.setLibelle(body.get("libelle"));

        return ResponseEntity.ok(parametrageModuleUEService.addUE(ue));
    }

    // ----------------- MENTION MODULE ENDPOINTS
    @PostMapping("mention-ue")
    public ResponseEntity<?> addMentionUE(@RequestBody List<MentionUE> mentionUEs) {
        if (mentionUEs == null)
            throw new BadRequestException("body is required");
        if (mentionUEs.isEmpty())
            throw new BadRequestException("body is required");

        mentionUEs.parallelStream().forEach(d -> parametrageModuleUEService.addMentionUE(d));
        return ResponseEntity.status(HttpStatus.CREATED).body(mentionUEs);
    }

    @GetMapping("mention-ue/ue/{id}")
    public ResponseEntity<?> getAllMentionUEByUE(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        UE ue = parametrageModuleUEService.findUEById(id);
        if (ue == null)
            throw new EntityNotFoundException("entity not found");

        return ResponseEntity.ok(parametrageModuleUEService.findAllMentionUEByUE(ue));
    }

    @DeleteMapping("mention-ue/{id}")
    public ResponseEntity<?> archiveMentionUE(@PathVariable Long id) {
        if (id == null)
            throw new BadRequestException("id required");
        MentionUE mentionUE = parametrageModuleUEService.findMentionUEById(id);
        if (mentionUE == null)
            throw new EntityNotFoundException("entity not found");

        mentionUE.setArchive(true);
        return ResponseEntity.ok(parametrageModuleUEService.addMentionUE(mentionUE));
    }

    @GetMapping("mention-ue/mention/{specialiteId}")
    public ResponseEntity<?> getAllMentionUEByMention(@PathVariable Long specialiteId) {
        if (specialiteId == null)
            throw new BadRequestException("specialiteId required");
        Specialite specialite = parametrageSpecialiteService.findSpecialiteById(specialiteId);
        if (specialite == null)
            throw new EntityNotFoundException("specialite not found");

        return ResponseEntity.ok(parametrageModuleUEService.findAllMentionUEByMentions(specialite.getMention()));
    }

}
