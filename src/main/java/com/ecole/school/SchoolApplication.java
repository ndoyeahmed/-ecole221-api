package com.ecole.school;

import javax.annotation.Resource;

import com.ecole.school.services.utils.ExcelWriter;
import com.ecole.school.services.utils.FileStorageService;

import com.ecole.school.services.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SchoolApplication implements CommandLineRunner {

	@Resource
	FileStorageService storageService;
	@Autowired
	private Utils utils;
	@Autowired
	private ExcelWriter excelWriter;

	public static void main(String[] args) {
		SpringApplication.run(SchoolApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		System.out.println(utils.generateUECode("Système Informations", 1, 1, 4));
		storageService.deleteAll();
		storageService.init();
		excelWriter.generateReferentielUploadModel();
	}
}
