
package com.imgresident.auth.dto;

import lombok.Data;

@Data
public class AuthResponse {
  private String token;
  private UserDto user;

  public static class UserDto {
    public String email;
    public String firstName;
    public String lastName;
  }


}
