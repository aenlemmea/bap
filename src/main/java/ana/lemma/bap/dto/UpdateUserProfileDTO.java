package ana.lemma.bap.dto;

import ana.lemma.bap.model.Role;

public record UpdateUserProfileDTO(String email,
                                   String password,
                                   String fullName,
                                   Role role) {}
