package ai.dfx.labs.microlarp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Instance.
 */
@Entity
@Table(name = "instance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Instance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("instances")
    private Game game;

    @ManyToMany(mappedBy = "instances")
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

    public Game getGame() {
        return game;
    }

    public Instance game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Organizer> getOrganizers() {
        return organizers;
    }

    public Instance organizers(Set<Organizer> organizers) {
        this.organizers = organizers;
        return this;
    }

    public Instance addOrganizer(Organizer organizer) {
        this.organizers.add(organizer);
        organizer.getInstances().add(this);
        return this;
    }

    public Instance removeOrganizer(Organizer organizer) {
        this.organizers.remove(organizer);
        organizer.getInstances().remove(this);
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
        if (!(o instanceof Instance)) {
            return false;
        }
        return id != null && id.equals(((Instance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Instance{" +
            "id=" + getId() +
            "}";
    }
}
