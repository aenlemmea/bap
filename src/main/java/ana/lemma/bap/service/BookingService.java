package ana.lemma.bap.service;

import ana.lemma.bap.dto.BookingResponseDTO;
import ana.lemma.bap.dto.CreateBookingRequestDTO;
import ana.lemma.bap.exception.BookingConflictException;
import ana.lemma.bap.exception.ResourceNotFoundException;
import ana.lemma.bap.model.Booking;
import ana.lemma.bap.model.BookingStatus;
import ana.lemma.bap.model.Listing;
import ana.lemma.bap.model.User;
import ana.lemma.bap.repository.BookingRepository;
import ana.lemma.bap.repository.ListingRepository;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

  private final ListingRepository listingRepository;
  private final BookingRepository bookingRepository;

  public BookingService(BookingRepository bookingRepository, ListingRepository listingRepository) {
    this.bookingRepository = bookingRepository;
    this.listingRepository = listingRepository;
  }

  public BookingResponseDTO getBookingById(Long bookingId) {
    Booking booking =
        bookingRepository
            .findById(bookingId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Booking with given booking Id: " + bookingId + " not found."));
    return toResponseDTO(booking);
  }

  public List<BookingResponseDTO> getAllBookings() {
    return bookingRepository.findAll().stream().map(this::toResponseDTO).toList();
  }

  @Transactional
  public BookingResponseDTO createBooking(CreateBookingRequestDTO requestDTO) {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Listing listing =
        listingRepository
            .findByIdWithLock(requestDTO.listingId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Listing not found with given Id: " + requestDTO.listingId()));

    if (!requestDTO.checkOutDate().isAfter(requestDTO.checkInDate())) {
      throw new BookingConflictException("Checkout date must be after check-in date");
    }

    if (requestDTO.guestCount() > listing.getMaxGuests()) {
      throw new BookingConflictException("Guest count exceeds listing capacity");
    }

    boolean overlap =
        bookingRepository.existsByListingIdAndStatusInAndCheckInDateBeforeAndCheckOutDateAfter(
            listing.getId(),
            List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING),
            requestDTO.checkOutDate(),
            requestDTO.checkInDate());

    if (overlap) {
      throw new BookingConflictException("Listing is already booked for the selected dates");
    }

    long nights = ChronoUnit.DAYS.between(requestDTO.checkInDate(), requestDTO.checkOutDate());

    BigDecimal totalPrice =
        BigDecimal.valueOf(nights).multiply(BigDecimal.valueOf(listing.getPricePerNight()));

    Booking booking = new Booking();

    booking.setListing(listing);
    booking.setGuest(currentUser);
    booking.setGuestCount(requestDTO.guestCount());
    booking.setCheckInDate(requestDTO.checkInDate());
    booking.setCheckOutDate(requestDTO.checkOutDate());
    booking.setTotalPrice(totalPrice);
    booking.setStatus(BookingStatus.PENDING);

    Booking savedBooking = bookingRepository.save(booking);

    return toResponseDTO(savedBooking);
  }

  public BookingResponseDTO toResponseDTO(Booking booking) {
    return new BookingResponseDTO(
        booking.getId(),
        booking.getListing().getId(),
        booking.getCheckInDate(),
        booking.getCheckOutDate(),
        booking.getTotalPrice(),
        booking.getGuestCount(),
        booking.getStatus());
  }

  // TODO
  public List<BookingResponseDTO> getUserBookings() {
    return null;
  }

  // TODO
  public BookingResponseDTO cancelBooking(Long id) {
    return null;
  }

  // TODO
  public BookingResponseDTO updateStatus(Long id, String status) {
    return null;
  }
}
