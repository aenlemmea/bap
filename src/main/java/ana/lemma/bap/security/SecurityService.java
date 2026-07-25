package ana.lemma.bap.security;

import ana.lemma.bap.exception.ResourceNotFoundException;
import ana.lemma.bap.model.Booking;
import ana.lemma.bap.model.Listing;
import ana.lemma.bap.model.User;
import ana.lemma.bap.repository.BookingRepository;
import ana.lemma.bap.repository.ListingRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

  private final ListingRepository listingRepository;
  private final BookingRepository bookingRepository;

  public SecurityService(ListingRepository listingRepository, BookingRepository bookingRepository) {
    this.listingRepository = listingRepository;
    this.bookingRepository = bookingRepository;
  }

  public boolean isListingOwner(Long listingId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user == null) {
      return false;
    }

    Listing listing =
        listingRepository
            .findByIdWithLock(listingId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "No listing found with given listing Id: " + listingId));

    return listing.getHost().getId().equals(user.getId());
  }

  public boolean isGuestorHost(Long bookingId) {
    Object principal =
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!(principal instanceof User user)) {
      return false;
    }

    Booking booking =
        bookingRepository
            .findById(bookingId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "No booking found with given booking Id: " + bookingId));

    return booking.getGuest() == user || booking.getListing().getHost() == user;
  }
}
