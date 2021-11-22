package et.com.hmmk.rmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "strategic_objective")
    private String strategicObjective;

    @Column(name = "future_focus_area")
    private String futureFocusArea;

    @Column(name = "current_funding_cycle")
    private String currentFundingCycle;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "companies" }, allowSetters = true)
    private TypeOfOrganization typeOfOrganation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrategicObjective() {
        return this.strategicObjective;
    }

    public Company strategicObjective(String strategicObjective) {
        this.setStrategicObjective(strategicObjective);
        return this;
    }

    public void setStrategicObjective(String strategicObjective) {
        this.strategicObjective = strategicObjective;
    }

    public String getFutureFocusArea() {
        return this.futureFocusArea;
    }

    public Company futureFocusArea(String futureFocusArea) {
        this.setFutureFocusArea(futureFocusArea);
        return this;
    }

    public void setFutureFocusArea(String futureFocusArea) {
        this.futureFocusArea = futureFocusArea;
    }

    public String getCurrentFundingCycle() {
        return this.currentFundingCycle;
    }

    public Company currentFundingCycle(String currentFundingCycle) {
        this.setCurrentFundingCycle(currentFundingCycle);
        return this;
    }

    public void setCurrentFundingCycle(String currentFundingCycle) {
        this.currentFundingCycle = currentFundingCycle;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company user(User user) {
        this.setUser(user);
        return this;
    }

    public TypeOfOrganization getTypeOfOrganation() {
        return this.typeOfOrganation;
    }

    public void setTypeOfOrganation(TypeOfOrganization typeOfOrganization) {
        this.typeOfOrganation = typeOfOrganization;
    }

    public Company typeOfOrganation(TypeOfOrganization typeOfOrganization) {
        this.setTypeOfOrganation(typeOfOrganization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", strategicObjective='" + getStrategicObjective() + "'" +
            ", futureFocusArea='" + getFutureFocusArea() + "'" +
            ", currentFundingCycle='" + getCurrentFundingCycle() + "'" +
            "}";
    }
}
