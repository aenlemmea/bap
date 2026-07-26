package ana.lemma.bap.dto;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "users", itemRelation = "user")
public record AuthorizeResponseDTO(String fullName, String email, String role) {}
