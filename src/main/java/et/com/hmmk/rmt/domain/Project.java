package et.com.hmmk.rmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "answers", "user", "form", "project" }, allowSetters = true)
    private Set<FormProgresss> formProgresses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "projects", "typeOfOrganation" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Project projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return this.projectDescription;
    }

    public Project projectDescription(String projectDescription) {
        this.setProjectDescription(projectDescription);
        return this;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Set<FormProgresss> getFormProgresses() {
        return this.formProgresses;
    }

    public void setFormProgresses(Set<FormProgresss> formProgressses) {
        if (this.formProgresses != null) {
            this.formProgresses.forEach(i -> i.setProject(null));
        }
        if (formProgressses != null) {
            formProgressses.forEach(i -> i.setProject(this));
        }
        this.formProgresses = formProgressses;
    }

    public Project formProgresses(Set<FormProgresss> formProgressses) {
        this.setFormProgresses(formProgressses);
        return this;
    }

    public Project addFormProgress(FormProgresss formProgresss) {
        this.formProgresses.add(formProgresss);
        formProgresss.setProject(this);
        return this;
    }

    public Project removeFormProgress(FormProgresss formProgresss) {
        this.formProgresses.remove(formProgresss);
        formProgresss.setProject(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Project company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", projectName='" + getProjectName() + "'" +
            ", projectDescription='" + getProjectDescription() + "'" +
            "}";
    }
}
