package ai.dfx.labs.microlarp.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Organizer.
 */
@Entity
@Table(name = "organizer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organizer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "organizer_game",
               joinColumns = @JoinColumn(name = "organizer_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"))
    private Set<Game> games = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "organizer_instance",
               joinColumns = @JoinColumn(name = "organizer_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "instance_id", referencedColumnName = "id"))
    private Set<Instance> instances = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "organizer_ruleset",
               joinColumns = @JoinColumn(name = "organizer_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "ruleset_id", referencedColumnName = "id"))
    private Set<Ruleset> rulesets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Game> getGames() {
        return games;
    }

    public Organizer games(Set<Game> games) {
        this.games = games;
        return this;
    }

    public Organizer addGame(Game game) {
        this.games.add(game);
        game.getOrganizers().add(this);
        return this;
    }

    public Organizer removeGame(Game game) {
        this.games.remove(game);
        game.getOrganizers().remove(this);
        return this;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<Instance> getInstances() {
        return instances;
    }

    public Organizer instances(Set<Instance> instances) {
        this.instances = instances;
        return this;
    }

    public Organizer addInstance(Instance instance) {
        this.instances.add(instance);
        instance.getOrganizers().add(this);
        return this;
    }

    public Organizer removeInstance(Instance instance) {
        this.instances.remove(instance);
        instance.getOrganizers().remove(this);
        return this;
    }

    public void setInstances(Set<Instance> instances) {
        this.instances = instances;
    }

    public Set<Ruleset> getRulesets() {
        return rulesets;
    }

    public Organizer rulesets(Set<Ruleset> rulesets) {
        this.rulesets = rulesets;
        return this;
    }

    public Organizer addRuleset(Ruleset ruleset) {
        this.rulesets.add(ruleset);
        ruleset.getOrganizers().add(this);
        return this;
    }

    public Organizer removeRuleset(Ruleset ruleset) {
        this.rulesets.remove(ruleset);
        ruleset.getOrganizers().remove(this);
        return this;
    }

    public void setRulesets(Set<Ruleset> rulesets) {
        this.rulesets = rulesets;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organizer)) {
            return false;
        }
        return id != null && id.equals(((Organizer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Organizer{" +
            "id=" + getId() +
            "}";
    }
}
