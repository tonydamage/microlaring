package ai.dfx.labs.microlarp.repository;
import ai.dfx.labs.microlarp.domain.Organizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Organizer entity.
 */
@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    @Query(value = "select distinct organizer from Organizer organizer left join fetch organizer.games left join fetch organizer.instances left join fetch organizer.rulesets",
        countQuery = "select count(distinct organizer) from Organizer organizer")
    Page<Organizer> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct organizer from Organizer organizer left join fetch organizer.games left join fetch organizer.instances left join fetch organizer.rulesets")
    List<Organizer> findAllWithEagerRelationships();

    @Query("select organizer from Organizer organizer left join fetch organizer.games left join fetch organizer.instances left join fetch organizer.rulesets where organizer.id =:id")
    Optional<Organizer> findOneWithEagerRelationships(@Param("id") Long id);

}
