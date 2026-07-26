package ana.lemma.bap.controller.v2;

import ana.lemma.bap.controller.v2.assembler.BookingModelAssembler;
import ana.lemma.bap.dto.BookingResponseDTO;
import ana.lemma.bap.dto.CreateBookingRequestDTO;
import ana.lemma.bap.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/bookings")
public class BookingControllerV2 {
  private final BookingService bookingService;

  private final BookingModelAssembler bookingModelAssembler;

  public BookingControllerV2(
      BookingService bookingService, BookingModelAssembler bookingModelAssembler) {
    this.bookingService = bookingService;
    this.bookingModelAssembler = bookingModelAssembler;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public CollectionModel<EntityModel<BookingResponseDTO>> getAllBookings() {
    return bookingModelAssembler.toCollectionModel(bookingService.getAllBookings());
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public EntityModel<BookingResponseDTO> getBookingById(Long id) {
    return bookingModelAssembler.toModel(bookingService.getBookingById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('GUEST')")
  public ResponseEntity<EntityModel<BookingResponseDTO>> createBooking(
      @RequestBody @Valid CreateBookingRequestDTO bookingRequestDTO) {
    BookingResponseDTO booking = bookingService.createBooking(bookingRequestDTO);
    EntityModel<BookingResponseDTO> bookingModelAssemblerModel =
        bookingModelAssembler.toModel(booking);
    return ResponseEntity.created(bookingModelAssemblerModel.getRequiredLink("self").toUri())
        .body(bookingModelAssemblerModel);
  }
}
