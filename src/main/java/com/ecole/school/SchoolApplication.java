package com.ecole.school;

import javax.annotation.Resource;

import com.ecole.school.models.ProgrammeModule;
import com.ecole.school.models.ProgrammeUE;
import com.ecole.school.services.inscription.InscriptionService;
import com.ecole.school.services.parametrages.ParametrageBaseService;
import com.ecole.school.services.parametrages.ParametrageReferentielService;
import com.ecole.school.services.utils.ExcelWriter;
import com.ecole.school.services.utils.FileStorageService;

import com.ecole.school.services.utils.Utils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@Log
@SpringBootApplication
@EnableAsync
public class SchoolApplication extends SpringBootServletInitializer implements CommandLineRunner{

	@Resource
	FileStorageService storageService;
	@Autowired
	private Utils utils;
	@Autowired
	private ExcelWriter excelWriter;
/*	@Autowired
	private ParametrageReferentielService parametrageReferentielService;*/

	public static void main(String[] args) {
		SpringApplication.run(SchoolApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SchoolApplication.class);
	}

	/**
	 * correct programme ue code and programme module code
	 */
	/*private void setCodeToProgrammeUE() {
		try {
			*//*List<ProgrammeUE> programmeUEList = parametrageReferentielService.findAllProgrammeUE();
			programmeUEList.forEach(programmeUE -> {
				programmeUE.setCode(
					utils.generateUECode(programmeUE.getUe().getCode(),
						programmeUE.getReferentiel().getNiveau().getNiveau(),
						programmeUE.getSemestre().getNumero(),
						programmeUE.getUe().getNumero()));
				parametrageReferentielService.addProgrammeUE(programmeUE);
			});*//*

			List<ProgrammeModule> programmeModuleList = parametrageReferentielService.findAllProgrammeModule();
			programmeModuleList.forEach(programmeModule -> {
				programmeModule.setCode(utils.generateModuleCode(
					programmeModule.getProgrammeUE().getCode(),
					Integer.parseInt(programmeModule.getNum())));

				parametrageReferentielService.addProgrammeModule(programmeModule);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public void run(String... args) throws Exception {
//		System.out.println(utils.generateUECode("Système Informations", 1, 1, 4));
		storageService.deleteAll();
		storageService.init();
		excelWriter.generateReferentielUploadModel();
//		 setCodeToProgrammeUE();
	}
}
