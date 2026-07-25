package ana.lemma.bap.controller;

import ana.lemma.bap.dto.BookingResponseDTO;
import ana.lemma.bap.dto.CreateBookingRequestDTO;
import ana.lemma.bap.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
  private final BookingService bookingService;

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @PreAuthorize("hasRole('GUEST')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookingResponseDTO createBooking(@Valid @RequestBody CreateBookingRequestDTO requestDTO) {
    return bookingService.createBooking(requestDTO);
  }
}
