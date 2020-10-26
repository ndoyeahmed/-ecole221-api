package com.ecole.school.web.controllers;

import java.util.Map;

import com.ecole.school.models.Semestre;
import com.ecole.school.pojos.SemestrePOJO;
import com.ecole.school.services.ParametrageSpecialiteService;
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
@RequestMapping("/api/parametrage-specialite/")
public class ParametrageSpecialiteController {
    private ParametrageSpecialiteService parametrageSpecialiteService;

    @Autowired
    public ParametrageSpecialiteController(ParametrageSpecialiteService parametrageSpecialiteService) {
        this.parametrageSpecialiteService = parametrageSpecialiteService;
    }

    // ----------------- DOMAINE ENDPOINTS
    @PostMapping("semestre")
    public ResponseEntity<?> addSemestre(@RequestBody SemestrePOJO semestrePOJO) {
        if (semestrePOJO == null)
            throw new BadRequestException("body is required");
        if (semestrePOJO.getLibelle() == null || semestrePOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle is required");

        Semestre semestre = new Semestre();
        semestre.setLibelle(semestrePOJO.getLibelle());
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
    public ResponseEntity<?> updateSemestre(@PathVariable Long id, @RequestBody SemestrePOJO semestrePOJO) {
        if (id == null)
            throw new BadRequestException("id required");
        if (semestrePOJO == null)
            throw new BadRequestException("body is required");
        if (semestrePOJO.getLibelle() == null || semestrePOJO.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        Semestre semestre = parametrageSpecialiteService.findSemestreById(id);
        if (semestre == null)
            throw new EntityNotFoundException("entity not found");

        semestre.setEtat(semestrePOJO.isEtat());
        semestre.setLibelle(semestrePOJO.getLibelle());

        return ResponseEntity.ok(parametrageSpecialiteService.addSemestre(semestre));
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
}
