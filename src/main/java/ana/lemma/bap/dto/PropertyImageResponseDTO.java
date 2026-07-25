package ana.lemma.bap.dto;

public record PropertyImageResponseDTO(
        Long id, Long listingId, String filename, String fileUrl, boolean primaryImage
) {}
