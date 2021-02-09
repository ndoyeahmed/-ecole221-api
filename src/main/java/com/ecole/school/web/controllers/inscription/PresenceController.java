package com.ecole.school.web.controllers.inscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ecole.school.models.AnneeScolaire;
import com.ecole.school.models.Inscription;
import com.ecole.school.models.Jour;
import com.ecole.school.models.Presence;
import com.ecole.school.services.inscription.InscriptionService;
import com.ecole.school.services.inscription.PresenceService;
import com.ecole.school.services.parametrages.ParametrageBaseService;
import com.ecole.school.web.exceptions.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/presence")
public class PresenceController {
    private InscriptionService inscriptionService;
    private PresenceService presenceService;
    private ParametrageBaseService parametrageBaseService;

    @Autowired
    public PresenceController(InscriptionService inscriptionService, PresenceService presenceService,
            ParametrageBaseService parametrageBaseService) {
        this.inscriptionService = inscriptionService;
        this.presenceService = presenceService;
        this.parametrageBaseService = parametrageBaseService;
    }

    public static Date getDateWithoutTimeUsingFormat() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(new Date()));
    }

    @PutMapping("/update-motif/{idPresence}")
    public ResponseEntity<?> updateMotifPresence(@RequestBody Map<String, String> motif, @PathVariable Long idPresence) {
        if (idPresence == null) throw new BadRequestException("id presence required");

        Presence presence = presenceService.findPresenceById(idPresence);
        if (presence == null) throw new BadRequestException("presence not found");

        presence.setMotif(motif.get("motif"));
        presenceService.savePresence(presence);

        return ResponseEntity.status(HttpStatus.CREATED).body(presence);
    }

    @PostMapping
    public ResponseEntity<?> addPresence(@RequestBody List<Presence> presences) throws ParseException {
        if (presences == null || presences.isEmpty())
            throw new BadRequestException("body required");
        if (presences.get(0).getInscription() == null)
            throw new BadRequestException("inscription required");

        Jour jour = presenceService.findJourByDate(getDateWithoutTimeUsingFormat());
        if (jour != null)
            throw new BadRequestException("presence already marked");

        jour = new Jour();
        jour.setDate(getDateWithoutTimeUsingFormat());
        jour.setJour(jour.getDate().getDay());
        jour = presenceService.saveJour(jour);

        for (Presence presence : presences) {
            presence.setJour(jour);
            presenceService.savePresence(presence);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(presences);
    }

    @GetMapping("/inscription/{idInscription}/etat/{etat}/anneescolaire/{idAnneeScolaire}")
    public ResponseEntity<?> getAllPresenceByInscriptionAndEtat(@PathVariable Long idInscription,
            @PathVariable Boolean etat, @PathVariable Long idAnneeScolaire) {
        if (idInscription == null)
            throw new BadRequestException("id inscription required");
        if (idAnneeScolaire == null)
            throw new BadRequestException("id annee scolaire required");
        if (etat == null)
            throw new BadRequestException("etat presence required");

        Inscription inscription = inscriptionService.findInscriptionById(idInscription);
        if (inscription == null)
            throw new BadRequestException("inscription not found");

        AnneeScolaire anneeScolaire = parametrageBaseService.findAnneeScolaireById(idAnneeScolaire);
        if (anneeScolaire == null)
            throw new BadRequestException("annee scolaire not found");

        return ResponseEntity.ok(
                presenceService.findAllPresenceByInscriptionAndEtatAndAnneeScolaire(inscription, etat, anneeScolaire));
    }
}
