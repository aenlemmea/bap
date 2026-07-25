package ana.lemma.bap.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateBookingRequestDTO(

        @NotNull(message = "Listing ID is required")
        Long listingId,

        @NotNull(message = "Check-in date is required")
        @FutureOrPresent(message = "Check-in date cannot be in the past")
        LocalDate checkInDate,

        @NotNull(message = "Check-out date is required")
        @FutureOrPresent(message = "Check-out date cannot be in the past")
        LocalDate checkOutDate,

        @NotNull(message = "Guest count is required")
        @Min(value = 1, message = "At least one guest is required")
        Integer guestCount

) {}