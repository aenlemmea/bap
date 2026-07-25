package ana.lemma.bap.service;

import ana.lemma.bap.dto.AuthorizeResponseDTO;
import ana.lemma.bap.dto.AuthorizeServiceResultDTO;
import ana.lemma.bap.dto.LoginRequestDTO;
import ana.lemma.bap.dto.RegisterUserRequestDTO;
import ana.lemma.bap.exception.BookingConflictException;
import ana.lemma.bap.model.Role;
import ana.lemma.bap.model.User;
import ana.lemma.bap.repository.UserRepository;
import ana.lemma.bap.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private JwtService jwtService;

  public AuthorizeService(
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public void register(RegisterUserRequestDTO requestDTO) {
    if (userRepository.findByEmail(requestDTO.email()).isPresent())
      throw new BookingConflictException("User already registered");

    User user = new User();
    user.setEmail(requestDTO.email());
    user.setFullName(requestDTO.fullName());
    user.setPassword(passwordEncoder.encode(requestDTO.password()));

    user.setRole(requestDTO.role() == Role.ROLE_ADMIN ? Role.ROLE_GUEST : requestDTO.role());
    userRepository.save(user);
  }

  public AuthorizeServiceResultDTO login(LoginRequestDTO loginRequestDTO) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequestDTO.email(), loginRequestDTO.password()));

    User user =
        userRepository
            .findByEmail(loginRequestDTO.email())
            .orElseThrow(() -> new UsernameNotFoundException("User with provided email not found"));

    AuthorizeResponseDTO responseDTO =
        new AuthorizeResponseDTO(user.getFullName(), user.getEmail(), user.getRole().name());
    return new AuthorizeServiceResultDTO(jwtService.generateToken(user), responseDTO);
  }
}
