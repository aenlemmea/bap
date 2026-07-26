package ana.lemma.bap.controller.v2.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import ana.lemma.bap.controller.v2.BookingControllerV2;
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
    return null;
//            EntityModel.of(
//        bookingResponseDTO,
//        linkTo(methodOn(BookingControllerV2.class).getBookingById(bookingResponseDTO.id()))
//            .withSelfRel(),
//        linkTo(methodOn(BookingControllerV2.class).getLisingById(bookingResponseDTO.listingId()))
//            .withRel("listing"));
  }

  @Override
  public CollectionModel<EntityModel<BookingResponseDTO>> toCollectionModel(
      Iterable<? extends BookingResponseDTO> entities) {
    return RepresentationModelAssembler.super.toCollectionModel(entities);
  }
}
