package com.ecole.school.web.controllers.inscription;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.ecole.school.services.utils.FileInfo;
import com.ecole.school.services.utils.FileStorageService;
import com.ecole.school.web.POJO.ResponseMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@CrossOrigin
@RequestMapping("/api/files-storage")
public class FilesController {

    private FileStorageService storageService;

    @Autowired
    public FilesController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      storageService.save(file);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/files")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = storageService.load(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @PutMapping("/files/base64")
  public ResponseEntity<?> getImages(@RequestBody String filename) {
    try {

      final InputStream inputStream = new FileInputStream(new File(filename));
      final byte[] bytes = IOUtils.toByteArray(inputStream);
      final String encodedImage = Base64.getEncoder().encodeToString(bytes);

      return ResponseEntity.ok(Collections.singletonMap("response", encodedImage));

    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new RuntimeException("oops!");
  }

  @GetMapping(value = "/referentiel-upload-model/download")
  public void getResource(HttpServletResponse response) throws IOException, IOException {

    String fileLocation=System.getProperty("user.home") + "/ecole221files/referentiel/model/Referentiel_upload_Model.xlsx";

            File downloadFile= new File(fileLocation);

    byte[] isr = Files.readAllBytes(downloadFile.toPath());
    ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
    out.write(isr, 0, isr.length);

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    // Use 'inline' for preview and 'attachement' for download in browser.
    response.addHeader("Content-Disposition", "attachment; filename=Referentiel_upload_Model.xlsx");

    OutputStream os;
    try {
      os = response.getOutputStream();
      out.writeTo(os);
      os.flush();
      os.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    /*HttpHeaders respHeaders = new HttpHeaders();
    respHeaders.setContentLength(isr.length);
    respHeaders.setContentType(new MediaType("text", "json"));
    respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
    return new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK);*/
  }
}
