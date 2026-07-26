package ana.lemma.bap.controller;

import ana.lemma.bap.dto.*;
import ana.lemma.bap.service.AuthorizeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
  private final AuthorizeService authorizeService;
  private static final String JWT_COOKIE = "jwt_token";

  public UserController(AuthorizeService authorizeService) {
    this.authorizeService = authorizeService;
  }


  @GetMapping("/me")
  public UserProfileResponseDTO getCurrentUser() {
    return authorizeService.getCurrentUserProfile();
  }

  @PatchMapping("/me")
  public UserProfileResponseDTO updateProfile(@RequestBody UpdateUserProfileDTO dto) {
    return authorizeService.updateCurrentUserProfile(dto);
  }


  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void register(@Valid @RequestBody RegisterUserRequestDTO requestDTO) {
    authorizeService.register(requestDTO);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public AuthorizeResponseDTO login(
      @Valid @RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) {
    // return authorizeService.login(requestDTO);
    AuthorizeServiceResultDTO authorizeServiceResultDTO = authorizeService.login(requestDTO);

    // NOTE: Change in deployment
    ResponseCookie cookie =
        ResponseCookie.from(JWT_COOKIE, authorizeServiceResultDTO.token())
            .httpOnly(true)
            .secure(false) // https
            .path("/")
            .maxAge(Duration.ofDays(1))
            .sameSite("Lax")
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    return authorizeServiceResultDTO.responseDTO();
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  public void logout(HttpServletResponse response) {
    ResponseCookie cookie =
        ResponseCookie.from(JWT_COOKIE, "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ZERO)
            .sameSite("Lax")
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
