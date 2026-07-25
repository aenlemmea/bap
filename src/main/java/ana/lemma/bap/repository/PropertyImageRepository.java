package ana.lemma.bap.repository;

import ana.lemma.bap.model.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage,
        Long> {
    List<PropertyImage> findByListingId(Long listingId);
}
