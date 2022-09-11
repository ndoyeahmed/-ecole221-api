package com.ecole.school.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecole.school.models.Utilisateur;
import com.ecole.school.repositories.UtilisateurRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

  private UtilisateurRepository utilisateurRepository;

  @Autowired
  public void setUtilisateurRepository(UtilisateurRepository utilisateurRepository) {
    this.utilisateurRepository = utilisateurRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    Optional<Utilisateur> userSearch = utilisateurRepository.connexion(login, true);

    return userSearch.map(utilisateur -> {
      List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
      grantedAuthorities.add(new SimpleGrantedAuthority(utilisateur.getProfil().getLibelle()));
      return new User(login, utilisateur.getPassword(), grantedAuthorities);
    }).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur " + login + " n'existe pas!"));

  }
}
