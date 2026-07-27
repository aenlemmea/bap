package ana.lemma.bap.repository;

import ana.lemma.bap.model.Listing;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ListingRepository extends JpaRepository<Listing, Long> {
  Page<Listing> findByAvailableTrue(Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT l FROM Listing l WHERE l.id = :id")
  Optional<Listing> findByIdWithLock(@Param("id") Long id);
}
