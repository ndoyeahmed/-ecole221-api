package com.ecole.school.web.controllers.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.ecole.school.models.Utilisateur;
import com.ecole.school.services.security.JwtUtil;
import com.ecole.school.services.security.MyUserDetailsService;
import com.ecole.school.services.userconfig.UtilisateurService;
import com.ecole.school.services.utils.Utilitaire;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

  private AuthenticationManager authenticationManager;
  private MyUserDetailsService userDetailsService;
  private JwtUtil jwtUtil;
  private UtilisateurService utilisateurService;
  private Utilitaire utilitaire;

  @Autowired
  public AuthenticationController(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService,
    JwtUtil jwtUtil, UtilisateurService utilisateurService, Utilitaire utilitaire) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtUtil = jwtUtil;
    this.utilisateurService = utilisateurService;
    this.utilitaire = utilitaire;
  }

  @PostMapping("/login")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

  @GetMapping("/connected-user")
  public MappingJacksonValue connectedUser() {
    try {
      Utilisateur utilisateur = utilisateurService.connectedUser();
      return utilitaire.getFilter(utilisateur, "passwordFilter", "password");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
