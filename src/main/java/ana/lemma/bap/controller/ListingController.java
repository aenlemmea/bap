package ana.lemma.bap.controller;

import ana.lemma.bap.dto.CreateListingRequestDTO;
import ana.lemma.bap.dto.ListingResponseDTO;
import ana.lemma.bap.dto.PropertyImageResponseDTO;
import ana.lemma.bap.dto.UpdateListingRequestDTO;
import ana.lemma.bap.service.ImageService;
import ana.lemma.bap.service.ListingService;
import jakarta.validation.Valid;
import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {
  private final ListingService listingService;
  private final ImageService imageService;

  public ListingController(ListingService listingService, ImageService imageService) {
    this.listingService = listingService;
    this.imageService = imageService;
  }

  // TODO Expose searchability, location awareness.

  @GetMapping
  public Page<ListingResponseDTO> getAllListings(
      @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable) {
    return listingService.getAvailableListings(pageable);
  }

  @PreAuthorize("hasAnyRole('HOST', 'ADMIN') and @securityService.isListingOwner(#listingId)")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ListingResponseDTO createListing(
      @Valid @RequestBody CreateListingRequestDTO createListingRequestDTO) {
    return listingService.createListing(createListingRequestDTO);
  }

  @PreAuthorize(
      "hasRole('ADMIN') or (hasRole('HOST') and @securityService.isListingOwner(#listingId))")
  @PutMapping("/{listingId}")
  public ListingResponseDTO updateListing(
      @PathVariable Long listingId, @Valid @RequestBody UpdateListingRequestDTO updateListingDTO) {
    return listingService.updateListing(listingId, updateListingDTO);
  }

  @PreAuthorize(
      "hasRole('ADMIN') or (hasRole('HOST') and @securityService.isListingOwner(#listingId))")
  @DeleteMapping("/{listingId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteListing(@PathVariable Long listingId) {
    listingService.deleteListing(listingId);
  }

  @PreAuthorize(
      "hasRole('ADMIN') or (hasRole('HOST') and @securityService.isListingOwner(#listingId))")
  @PostMapping(value = "/{listingId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public PropertyImageResponseDTO uploadImage(
      @PathVariable Long listingId,
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "isPrimary", defaultValue = "false") boolean isPrimary)
      throws IOException {
    return imageService.uploadListingImage(listingId, file, isPrimary);
  }

  @GetMapping("/{listingId}")
  public ListingResponseDTO getListingById(@PathVariable Long listingId) {
    return listingService.getListingById(listingId);
  }
}
