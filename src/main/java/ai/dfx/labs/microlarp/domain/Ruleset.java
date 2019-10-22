package ai.dfx.labs.microlarp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ruleset.
 */
@Entity
@Table(name = "ruleset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ruleset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "ruleset_game",
               joinColumns = @JoinColumn(name = "ruleset_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"))
    private Set<Game> games = new HashSet<>();

    @ManyToMany(mappedBy = "rulesets")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Organizer> organizers = new HashSet<>();

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

    public Ruleset games(Set<Game> games) {
        this.games = games;
        return this;
    }

    public Ruleset addGame(Game game) {
        this.games.add(game);
        game.getRulesets().add(this);
        return this;
    }

    public Ruleset removeGame(Game game) {
        this.games.remove(game);
        game.getRulesets().remove(this);
        return this;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<Organizer> getOrganizers() {
        return organizers;
    }

    public Ruleset organizers(Set<Organizer> organizers) {
        this.organizers = organizers;
        return this;
    }

    public Ruleset addOrganizer(Organizer organizer) {
        this.organizers.add(organizer);
        organizer.getRulesets().add(this);
        return this;
    }

    public Ruleset removeOrganizer(Organizer organizer) {
        this.organizers.remove(organizer);
        organizer.getRulesets().remove(this);
        return this;
    }

    public void setOrganizers(Set<Organizer> organizers) {
        this.organizers = organizers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ruleset)) {
            return false;
        }
        return id != null && id.equals(((Ruleset) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ruleset{" +
            "id=" + getId() +
            "}";
    }
}
