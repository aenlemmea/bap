package ana.lemma.bap.dto;


import jakarta.validation.constraints.*;

public record CreateListingRequestDTO(

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String description,

        @NotNull(message = "Price per night is required")
        @Positive(message = "Price per night must be positive")
        Double pricePerNight,  // Wrapper allows detect missing fields JSON,

        @NotBlank(message = "Location is required")
        @Size(max = 255)
        String location,

        @NotNull(message = "Maximum guests is required")
        @Min(value = 1, message = "There must be at least one guest")
        @Max(value = 100, message = "Maximum guests cannot exceed 100")
        Integer maxGuests

) {}
