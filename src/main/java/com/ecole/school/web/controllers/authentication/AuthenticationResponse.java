package com.ecole.school.web.controllers.authentication;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
  private String jwt;
}
