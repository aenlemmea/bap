package ana.lemma.bap.controller.v2;

import ana.lemma.bap.controller.v2.assembler.ListingModelAssembler;
import ana.lemma.bap.dto.ListingResponseDTO;
import ana.lemma.bap.service.ListingService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  @ResponseStatus(HttpStatus.OK)
  public EntityModel<ListingResponseDTO> getListingById(@PathVariable Long id) {
    return listingModelAssembler.toModel(listingService.getListingById(id));
  }

//  @GetMapping
//  @ResponseStatus(HttpStatus.OK)
//  public CollectionModel<EntityModel<ListingResponseDTO>> getAllListings(Pageable pageable) {
//    return listingModelAssembler.toCollectionModel(listingService.getAvailableListings(pageable));
//  }
}
