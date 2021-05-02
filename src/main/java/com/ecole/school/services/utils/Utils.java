package com.ecole.school.services.utils;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.ecole.school.models.Etudiant;
import com.ecole.school.models.Specialite;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Utils {

  @Autowired
  private BCryptPasswordEncoder encoder;

  public String generateUniqueId() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest salt = MessageDigest.getInstance("SHA-256");
    salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
    String digest = bytesToHex(salt.digest());
    return digest;
  }

  public String bytesToHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder();
    for (byte b : bytes) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }

  public String createCode(String libelle) {

    return libelle.trim().substring(0, 3);
  }

  public String encodePassword(String password) {
    return encoder.encode(password);
  }

  // Generate a radom String
  public String generatePassword() {
    String generatedString = RandomStringUtils.random(10, true, true);
    return generatedString;
  }

  // generate matricule pour etudiant
  public String genereMatriculeEtudiant(String specialiteNUm, String annee, List<Etudiant> etudiants) {
    if (etudiants == null || etudiants.isEmpty()) {
      return specialiteNUm + annee + "000000001";
    } else {
      return specialiteNUm + annee + new DecimalFormat("00000000").format(etudiants.size() + 1);
    }
  }

  // generate num pour specialite
  public String genereNumSpecialite(List<Specialite> specialites) {
    if (specialites == null || specialites.isEmpty()) {
      return "01";
    } else {
      return new DecimalFormat("0").format(specialites.size() + 1);
    }
  }

  // generate QR Code
  public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
    QRCodeWriter barcodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  // generate code UE by niveau, semestre num and ue order num
  public String generateUECode(String ue, int niveau, int semestreNum, int ueOrderNum) {
    String[] content;
    String code = "";
    if (ue != null && !ue.equals("")) {
      content = ue.split(" ");
      System.out.println(Arrays.toString(content));
      if (content.length > 0) {
        if (content.length == 2) {
          code = code.concat(content[0].substring(0, 1)).toUpperCase();
          code = code.concat(content[1].substring(0, 2)).toUpperCase();
        } else if (content.length >= 3) {
          code = code.concat(content[0].substring(0, 1)).toUpperCase();
          code = code.concat(content[1].substring(0, 1)).toUpperCase();
          code = code.concat(content[content.length-1].substring(0, 1)).toUpperCase();
        }
      }
    }
    return code + " " + niveau + "" + semestreNum + "" + new DecimalFormat("00").format(ueOrderNum);
  }

  // generate Module code
  public String generateModuleCode(String ueCode, int moduleNumberOrder) {
    return moduleNumberOrder + ueCode;
  }
}
