package ana.lemma.bap.dto;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "propertyImages", itemRelation = "propertImage")
public record PropertyImageResponseDTO(
        Long id, Long listingId, String filename, String fileUrl, boolean primaryImage
) {}
