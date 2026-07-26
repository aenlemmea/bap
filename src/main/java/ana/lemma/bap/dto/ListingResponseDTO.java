package ana.lemma.bap.dto;

import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Relation(collectionRelation = "listings", itemRelation = "listing")
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
