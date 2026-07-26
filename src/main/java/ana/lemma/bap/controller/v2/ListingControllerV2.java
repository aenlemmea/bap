package ana.lemma.bap.controller.v2;

import ana.lemma.bap.controller.v2.assembler.ListingModelAssembler;
import ana.lemma.bap.dto.ListingResponseDTO;
import ana.lemma.bap.service.ListingService;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/listings")
public class ListingControllerV2 {
  private final ListingService listingService;

  private final ListingModelAssembler listingModelAssembler;

  public ListingControllerV2(
      ListingService listingService, ListingModelAssembler listingModelAssembler) {
    this.listingService = listingService;
    this.listingModelAssembler = listingModelAssembler;
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<ListingResponseDTO>> getListingById(@PathVariable Long id) {
    ListingResponseDTO listingResponseDTO = listingService.getListingById(id);
    return ResponseEntity.ok(listingModelAssembler.toModel(listingResponseDTO));
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<ListingResponseDTO>>> getAllListings() {
    List<ListingResponseDTO> listingResponseDTOList = listingService.getAvailableListings();
    return ResponseEntity.ok(listingModelAssembler.toCollectionModel(listingResponseDTOList));
  }
}
