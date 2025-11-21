
package com.imgresident.auth;

import java.util.Map;
import com.imgresident.auth.dto.*;
import com.imgresident.security.JwtService;
import com.imgresident.user.AuthProvider;
import com.imgresident.user.User;
import com.imgresident.user.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager,
                     JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public AuthResponse register(RegisterRequest req) {
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new IllegalArgumentException("Email already registered");
    }

    User user = new User();
    user.setEmail(req.getEmail());
    user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
    user.setFirstName(req.getFirstName());
    user.setLastName(req.getLastName());
    user.setProvider(AuthProvider.LOCAL);

    userRepository.save(user);

    return buildAuthResponse(user);
  }

  public AuthResponse login(LoginRequest req) {
    var authToken = new UsernamePasswordAuthenticationToken(
        req.getEmail(), req.getPassword());
    authenticationManager.authenticate(authToken);

    User user = userRepository.findByEmail(req.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

    return buildAuthResponse(user);
  }

  private AuthResponse buildAuthResponse(User user) {
    var claims = Map.<String, Object>of(
        "firstName", user.getFirstName(),
        "lastName", user.getLastName()
    );
    String token = jwtService.generateToken(user.getEmail(), claims);

    AuthResponse.UserDto userDto = new AuthResponse.UserDto();
    userDto.email = user.getEmail();
    userDto.firstName = user.getFirstName();
    userDto.lastName = user.getLastName();

    AuthResponse response = new AuthResponse();
    response.setToken(token);
    response.setUser(userDto);
    return response;
  }
}
