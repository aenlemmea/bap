package ana.lemma.bap.dto;

import ana.lemma.bap.model.Role;

public record UserProfileResponseDTO(String email, String fullName, Role role
) {}
