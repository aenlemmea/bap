package ana.lemma.bap.security;

import ana.lemma.bap.service.OwnUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final OwnUserDetailsService ownUserDetailsService;
  private final JwtService jwtService;
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String JWT_COOKIE = "jwt_token";

  public JwtAuthenticationFilter(
      OwnUserDetailsService ownUserDetailsService, JwtService jwtService) {
    this.ownUserDetailsService = ownUserDetailsService;
    this.jwtService = jwtService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    return path.startsWith("/api/v1/auth/")
        || path.startsWith("/api/v1/login")
        || path.startsWith("/api/v1/register");
  }

  // TODO Refactor. Extract JWT extraction separately.
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String jwt = null;
    final String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(BEARER_PREFIX.length());
    } else if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (JWT_COOKIE.equals(cookie.getName())) {
          jwt = cookie.getValue();
          break;
        }
      }
    }

    if (jwt == null) {
      filterChain.doFilter(request, response);
      return;
    }
    String email = null;
    try {
      email = jwtService.extractEmail(jwt);
    } catch (JwtException | IllegalArgumentException | IllegalStateException e) {
      filterChain.doFilter(request, response);
      return;
    }
    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = ownUserDetailsService.loadUserByUsername(email);
      if (jwtService.isValidToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
