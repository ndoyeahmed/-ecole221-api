package com.ecole.school.services.parametrages;

import java.util.ArrayList;
import java.util.List;

import com.ecole.school.models.MentionModule;
import com.ecole.school.models.MentionUE;
import com.ecole.school.models.Module;
import com.ecole.school.models.UE;
import com.ecole.school.repositories.MentionModuleRepository;
import com.ecole.school.repositories.MentionUeRepository;
import com.ecole.school.repositories.ModuleRepository;
import com.ecole.school.repositories.UeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;

@Service
@Transactional
@Log
public class ParametrageModuleUEService {
    private ModuleRepository moduleRepository;
    private MentionModuleRepository mentionModuleRepository;
    private UeRepository ueRepository;
    private MentionUeRepository mentionUeRepository;

    @Autowired
    public ParametrageModuleUEService(ModuleRepository moduleRepository,
            MentionModuleRepository mentionModuleRepository, UeRepository ueRepository,
            MentionUeRepository mentionUeRepository) {
        this.moduleRepository = moduleRepository;
        this.mentionModuleRepository = mentionModuleRepository;
        this.ueRepository = ueRepository;
        this.mentionUeRepository = mentionUeRepository;
    }

    // ----------------- MODULE SERVICES
    public Module addModule(Module module) {
        try {
            moduleRepository.save(module);
            return module;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public Module findModuleById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }

    public List<Module> findAllModule() {
        return moduleRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- MENTION MODULE SERVICES
    public MentionModule addMentionModule(MentionModule mentionModule) {
        try {
            mentionModuleRepository.save(mentionModule);
            return mentionModule;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public MentionModule findMentionModuleById(Long id) {
        return mentionModuleRepository.findById(id).orElse(null);
    }

    public List<MentionModule> findAllMentionModuleByModule(Module module) {
        return mentionModuleRepository.findAllByModuleAndArchiveFalse(module).orElse(new ArrayList<>());
    }

    // ----------------- UE SERVICES
    public UE addUE(UE ue) {
        try {
            ueRepository.save(ue);
            return ue;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public UE findUEById(Long id) {
        return ueRepository.findById(id).orElse(null);
    }

    public List<UE> findAllUE() {
        return ueRepository.findAllByArchiveFalse().orElse(new ArrayList<>());
    }

    // ----------------- MENTION UE SERVICES
    public MentionUE addMentionUE(MentionUE mentionUE) {
        try {
            mentionUeRepository.save(mentionUE);
            return mentionUE;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public MentionUE findMentionUEById(Long id) {
        return mentionUeRepository.findById(id).orElse(null);
    }

    public List<MentionUE> findAllMentionUEByModule(UE ue) {
        return mentionUeRepository.findAllByUeAndArchiveFalse(ue).orElse(new ArrayList<>());
    }
}
