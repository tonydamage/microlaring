package ai.dfx.labs.microlarp.web.rest;

import ai.dfx.labs.microlarp.domain.Ruleset;
import ai.dfx.labs.microlarp.repository.RulesetRepository;
import ai.dfx.labs.microlarp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ai.dfx.labs.microlarp.domain.Ruleset}.
 */
@RestController
@RequestMapping("/api")
public class RulesetResource {

    private final Logger log = LoggerFactory.getLogger(RulesetResource.class);

    private static final String ENTITY_NAME = "ruleset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RulesetRepository rulesetRepository;

    public RulesetResource(RulesetRepository rulesetRepository) {
        this.rulesetRepository = rulesetRepository;
    }

    /**
     * {@code POST  /rulesets} : Create a new ruleset.
     *
     * @param ruleset the ruleset to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ruleset, or with status {@code 400 (Bad Request)} if the ruleset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rulesets")
    public ResponseEntity<Ruleset> createRuleset(@RequestBody Ruleset ruleset) throws URISyntaxException {
        log.debug("REST request to save Ruleset : {}", ruleset);
        if (ruleset.getId() != null) {
            throw new BadRequestAlertException("A new ruleset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ruleset result = rulesetRepository.save(ruleset);
        return ResponseEntity.created(new URI("/api/rulesets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rulesets} : Updates an existing ruleset.
     *
     * @param ruleset the ruleset to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ruleset,
     * or with status {@code 400 (Bad Request)} if the ruleset is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ruleset couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rulesets")
    public ResponseEntity<Ruleset> updateRuleset(@RequestBody Ruleset ruleset) throws URISyntaxException {
        log.debug("REST request to update Ruleset : {}", ruleset);
        if (ruleset.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ruleset result = rulesetRepository.save(ruleset);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ruleset.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rulesets} : get all the rulesets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rulesets in body.
     */
    @GetMapping("/rulesets")
    public List<Ruleset> getAllRulesets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Rulesets");
        return rulesetRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /rulesets/:id} : get the "id" ruleset.
     *
     * @param id the id of the ruleset to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ruleset, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rulesets/{id}")
    public ResponseEntity<Ruleset> getRuleset(@PathVariable Long id) {
        log.debug("REST request to get Ruleset : {}", id);
        Optional<Ruleset> ruleset = rulesetRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ruleset);
    }

    /**
     * {@code DELETE  /rulesets/:id} : delete the "id" ruleset.
     *
     * @param id the id of the ruleset to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rulesets/{id}")
    public ResponseEntity<Void> deleteRuleset(@PathVariable Long id) {
        log.debug("REST request to delete Ruleset : {}", id);
        rulesetRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
