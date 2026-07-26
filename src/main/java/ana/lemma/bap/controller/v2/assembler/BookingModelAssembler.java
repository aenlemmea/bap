package ana.lemma.bap.controller.v2.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import ana.lemma.bap.controller.v2.BookingControllerV2;
import ana.lemma.bap.controller.v2.ListingControllerV2;
import ana.lemma.bap.dto.BookingResponseDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class BookingModelAssembler
    implements RepresentationModelAssembler<BookingResponseDTO, EntityModel<BookingResponseDTO>> {
  @Override
  public EntityModel<BookingResponseDTO> toModel(@NonNull BookingResponseDTO bookingResponseDTO) {
    return EntityModel.of(
        bookingResponseDTO,
        linkTo(methodOn(BookingControllerV2.class).getBookingById(bookingResponseDTO.id()))
            .withSelfRel(),
        linkTo(methodOn(ListingControllerV2.class).getListingById(bookingResponseDTO.listingId()))
            .withRel("listing"));
  }

  @Override
  public CollectionModel<EntityModel<BookingResponseDTO>> toCollectionModel(
      Iterable<? extends BookingResponseDTO> entities) {
    CollectionModel<EntityModel<BookingResponseDTO>> collectionModel =
        RepresentationModelAssembler.super.toCollectionModel(entities);
    return collectionModel.add(
        linkTo(methodOn(BookingControllerV2.class).getAllBookings()).withSelfRel());
  }
}
