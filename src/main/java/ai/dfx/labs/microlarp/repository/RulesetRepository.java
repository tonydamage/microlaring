package ai.dfx.labs.microlarp.repository;
import ai.dfx.labs.microlarp.domain.Ruleset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Ruleset entity.
 */
@Repository
public interface RulesetRepository extends JpaRepository<Ruleset, Long> {

    @Query(value = "select distinct ruleset from Ruleset ruleset left join fetch ruleset.games",
        countQuery = "select count(distinct ruleset) from Ruleset ruleset")
    Page<Ruleset> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct ruleset from Ruleset ruleset left join fetch ruleset.games")
    List<Ruleset> findAllWithEagerRelationships();

    @Query("select ruleset from Ruleset ruleset left join fetch ruleset.games where ruleset.id =:id")
    Optional<Ruleset> findOneWithEagerRelationships(@Param("id") Long id);

}
