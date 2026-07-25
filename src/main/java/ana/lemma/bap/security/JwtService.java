package ana.lemma.bap.security;

import ana.lemma.bap.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secret;

  @Value("${application.security.jwt.expiration-ms}")
  private long jwtExpirationMs;

  private SecretKey generateSecretKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(User user) {
    return generateToken(user, millisecondsToHours(jwtExpirationMs));
  }

  /**
   * Generate a JWT token
   *
   * @param user The user object to build the JWT for.
   * @param expirationInHours The time to expire in hours
   * @return The JWT token
   */
  public String generateToken(User user, long expirationInHours) {
    return Jwts.builder()
        .subject(user.getId().toString())
        .claim("email", user.getEmail())
        .claim("fullname", user.getFullName())
        .claim("role", user.getRole())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + hoursToMilliseconds(expirationInHours)))
        .signWith(generateSecretKey())
        .compact();
  }

  /**
   * Extracts the email from the JWT
   *
   * @param token The JWT token
   */
  public String extractEmail(String token) {
    return Jwts.parser()
        .verifyWith(generateSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("email", String.class);
  }

  private boolean isTokenExpired(String token) {
    Date expiration =
        Jwts.parser()
            .verifyWith(generateSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    return expiration.before(new Date());
  }

  public boolean isValidToken(String token, UserDetails userDetails) {
    String email = extractEmail(token);
    return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  /**
   * Converts hours to milliseconds.
   *
   * @param hours Number of hours.
   * @return Milliseconds representing the duration.
   */
  public static long hoursToMilliseconds(long hours) {
    return TimeUnit.HOURS.toMillis(hours);
  }

  /**
   * Converts milliseconds to hours.
   *
   * @param milliseconds Duration in milliseconds.
   * @return Number of hours.
   */
  public static long millisecondsToHours(long milliseconds) {
    return TimeUnit.MILLISECONDS.toHours(milliseconds);
  }
}
