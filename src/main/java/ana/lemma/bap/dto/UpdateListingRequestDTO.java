package ana.lemma.bap.dto;

import ana.lemma.bap.model.PropertyImage;
import ana.lemma.bap.model.User;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateListingRequestDTO(

        @NotBlank
        @Size(max = 100)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull
        @Positive
        Double pricePerNight,

        @NotBlank
        @Size(max = 255)
        String location,

        @NotNull
        @Min(1)
        @Max(100)
        Integer maxGuests,

        @NotNull
        Boolean available
) {}