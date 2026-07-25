package ana.lemma.bap.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ListingResponseDTO(
        Long id,
        String title,
        String description,
        boolean available,
        double pricePerNight,
        String location,
        LocalDateTime createdAt,
        int maxGuests,
        List<PropertyImageResponseDTO> imageUrls
) {}
