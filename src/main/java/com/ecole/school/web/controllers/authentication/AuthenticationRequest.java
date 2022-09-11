package com.ecole.school.web.controllers.authentication;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AuthenticationRequest {
  private String username;
  private String password;
}
