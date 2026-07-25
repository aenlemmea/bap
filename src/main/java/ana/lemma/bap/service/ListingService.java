package ana.lemma.bap.service;

import ana.lemma.bap.dto.CreateListingRequestDTO;
import ana.lemma.bap.dto.ListingResponseDTO;
import ana.lemma.bap.dto.PropertyImageResponseDTO;
import ana.lemma.bap.exception.UnauthorizedActionException;
import ana.lemma.bap.model.Listing;
import ana.lemma.bap.model.Role;
import ana.lemma.bap.model.User;
import ana.lemma.bap.repository.ListingRepository;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListingService {

  private final ListingRepository listingRepository;

  public ListingService(ListingRepository listingRepository) {
    this.listingRepository = listingRepository;
  }

  public List<ListingResponseDTO> getAvailableListings() {
    return listingRepository.findByAvailableTrue().stream().map(this::toResponseDTO).toList();
  }

  @Transactional
  public ListingResponseDTO createListing(CreateListingRequestDTO requestDTO) {

    User currentUser =
            (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (currentUser.getRole() != Role.ROLE_HOST) {
      throw new UnauthorizedActionException("Only hosts can create listing");
    }

    Listing listing = new Listing();

    listing.setTitle(requestDTO.title());
    listing.setDescription(requestDTO.description());
    listing.setPricePerNight(requestDTO.pricePerNight());
    listing.setLocation(requestDTO.location());
    listing.setMaxGuests(requestDTO.maxGuests());

    listing.setAvailable(true);
    listing.setHost(currentUser);
    Listing savedListing = listingRepository.save(listing);

    return toResponseDTO(savedListing);
  }

  private ListingResponseDTO toResponseDTO(Listing listing) {
    List<PropertyImageResponseDTO> imageResponseDTOS
            = listing.getPropertyImages()
            .stream()
            .map(image ->
                    new PropertyImageResponseDTO(
                            image.getId(),
                            image.getListing().getId(),
                            image.getFilename(),
                            image.getFileUrl(),
                            image.isPrimaryImage()
                    ))
            .toList();

    return new ListingResponseDTO(
        listing.getId(),
        listing.getTitle(),
        listing.getDescription(),
        listing.isAvailable(),
        listing.getPricePerNight(),
        listing.getLocation(),
        listing.getCreatedAt(),
        listing.getMaxGuests(),
        imageResponseDTOS
    );
  }
}
