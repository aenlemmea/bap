package ana.lemma.bap.exception;

import ana.lemma.bap.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
      ResourceNotFoundException exception, HttpServletRequest request) {
    ErrorResponseDTO errorDTO =
        new ErrorResponseDTO(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
  }

  @ExceptionHandler(BookingConflictException.class)
  public ResponseEntity<ErrorResponseDTO> handleBookingConflict(
      BookingConflictException exception, HttpServletRequest request) {
    ErrorResponseDTO errorDTO =
        new ErrorResponseDTO(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
  }

  @ExceptionHandler(UnauthorizedActionException.class)
  ResponseEntity<ErrorResponseDTO> handleUnauthorized(
      UnauthorizedActionException exception, HttpServletRequest request) {
    ErrorResponseDTO errorDTO =
        new ErrorResponseDTO(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            exception.getMessage(),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
  }
}
