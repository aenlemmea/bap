package ana.lemma.bap.controller.v2.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import ana.lemma.bap.controller.v2.ListingControllerV2;
import ana.lemma.bap.dto.ListingResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ListingModelAssembler
    implements RepresentationModelAssembler<ListingResponseDTO, EntityModel<ListingResponseDTO>> {

  // TODO: Fix fileUrl being tied to v1.
  @Override
  public EntityModel<ListingResponseDTO> toModel(ListingResponseDTO listingResponseDTO) {
    return EntityModel.of(
        listingResponseDTO,
        linkTo(methodOn(ListingControllerV2.class).getListingById(listingResponseDTO.id()))
            .withSelfRel());
//            , linkTo(methodOn(ListingControllerV2.class).getAllListings()).withRel("listings"));
  }

  @Override
  public CollectionModel<EntityModel<ListingResponseDTO>> toCollectionModel(
      Iterable<? extends ListingResponseDTO> listingResponseDTOS) {
    CollectionModel<EntityModel<ListingResponseDTO>> collectionModel =
        RepresentationModelAssembler.super.toCollectionModel(listingResponseDTOS);

    return collectionModel;
//            .add(linkTo(methodOn(ListingControllerV2.class).getAllListings()).withSelfRel());
  }
}
