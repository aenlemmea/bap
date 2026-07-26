package ana.lemma.bap.controller;

import ana.lemma.bap.dto.BookingResponseDTO;
import ana.lemma.bap.dto.CreateBookingRequestDTO;
import ana.lemma.bap.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
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

  @GetMapping("/{bookingId}")
  public BookingResponseDTO getBookingById(@PathVariable Long bookingId) {
    return bookingService.getBookingById(bookingId);
  }

  // For given user get all bookings.
  @GetMapping
  public List<BookingResponseDTO> getUserBookings() {
    return bookingService.getUserBookings();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{all}")
  public List<BookingResponseDTO> getAllBookings() {
    return bookingService.getAllBookings();
  }
  // GUEST Cancel
  @PreAuthorize("hasRole('ADMIN') or @securityService.isBookingOwner(#id)")
  @PatchMapping("/{id}/cancel")
  public BookingResponseDTO cancelBooking(@PathVariable Long id) {
    return bookingService.cancelBooking(id);
  }

  // HOST Approve or Reject
  @PreAuthorize("hasRole('ADMIN') or @securityService.isBookingHost(#id)")
  @PatchMapping("/{id}/status")
  public BookingResponseDTO updateBookingStatus(
      @PathVariable Long id, @RequestParam String status) {
    return bookingService.updateStatus(id, status);
  }
}
