package ana.lemma.bap.repository;

import ana.lemma.bap.model.Booking;
import ana.lemma.bap.model.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  List<Booking> findByListingIdAndStatusInAndCheckInDateBeforeAndCheckOutDateAfter(
      Long listingId, List<BookingStatus> statuses, LocalDate checkOut, LocalDate checkIn);

  boolean existsByListingIdAndStatusInAndCheckInDateBeforeAndCheckOutDateAfter(
      Long ListingId, List<BookingStatus> statuses, LocalDate checkOut, LocalDate checkIn);

  List<Booking> findByListingId(Long listingId);

  List<Booking> findByGuestId(Long guestId);

  List<Booking> findByStatus(BookingStatus status);

  List<Booking> findByListingIdAndStatus(
          Long listingId,
          BookingStatus status
  );


}
