package ai.dfx.labs.microlarp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Instance> instances = new HashSet<>();

    @ManyToMany(mappedBy = "games")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Organizer> organizers = new HashSet<>();

    @ManyToMany(mappedBy = "games")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Ruleset> rulesets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Instance> getInstances() {
        return instances;
    }

    public Game instances(Set<Instance> instances) {
        this.instances = instances;
        return this;
    }

    public Game addInstance(Instance instance) {
        this.instances.add(instance);
        instance.setGame(this);
        return this;
    }

    public Game removeInstance(Instance instance) {
        this.instances.remove(instance);
        instance.setGame(null);
        return this;
    }

    public void setInstances(Set<Instance> instances) {
        this.instances = instances;
    }

    public Set<Organizer> getOrganizers() {
        return organizers;
    }

    public Game organizers(Set<Organizer> organizers) {
        this.organizers = organizers;
        return this;
    }

    public Game addOrganizer(Organizer organizer) {
        this.organizers.add(organizer);
        organizer.getGames().add(this);
        return this;
    }

    public Game removeOrganizer(Organizer organizer) {
        this.organizers.remove(organizer);
        organizer.getGames().remove(this);
        return this;
    }

    public void setOrganizers(Set<Organizer> organizers) {
        this.organizers = organizers;
    }

    public Set<Ruleset> getRulesets() {
        return rulesets;
    }

    public Game rulesets(Set<Ruleset> rulesets) {
        this.rulesets = rulesets;
        return this;
    }

    public Game addRuleset(Ruleset ruleset) {
        this.rulesets.add(ruleset);
        ruleset.getGames().add(this);
        return this;
    }

    public Game removeRuleset(Ruleset ruleset) {
        this.rulesets.remove(ruleset);
        ruleset.getGames().remove(this);
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
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            "}";
    }
}
