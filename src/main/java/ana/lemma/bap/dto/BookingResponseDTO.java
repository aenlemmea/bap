package ana.lemma.bap.dto;

import ana.lemma.bap.model.BookingStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingResponseDTO(
        Long id,
        Long listingId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BigDecimal totalPrice,
        int guestCount,
        BookingStatus status
) {}