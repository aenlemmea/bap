package ana.lemma.bap.dto;

import ana.lemma.bap.model.BookingStatus;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Relation(collectionRelation = "bookings", itemRelation = "booking")
public record BookingResponseDTO(
        Long id,
        Long listingId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BigDecimal totalPrice,
        int guestCount,
        BookingStatus status
) {}