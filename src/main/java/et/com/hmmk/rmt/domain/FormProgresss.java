package et.com.hmmk.rmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FormProgresss.
 */
@Entity
@Table(name = "form_progresss")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FormProgresss implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "submited")
    private Boolean submited;

    @Column(name = "started_on")
    private Instant startedOn;

    @Column(name = "submited_on")
    private Instant submitedOn;

    @Column(name = "sented_on")
    private Instant sentedOn;

    @OneToMany(mappedBy = "formProgresss")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "multipleChoiceAnsewers", "user", "question", "formProgresss" }, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "formProgressses", "questions", "user" }, allowSetters = true)
    private Form form;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormProgresss id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSubmited() {
        return this.submited;
    }

    public FormProgresss submited(Boolean submited) {
        this.setSubmited(submited);
        return this;
    }

    public void setSubmited(Boolean submited) {
        this.submited = submited;
    }

    public Instant getStartedOn() {
        return this.startedOn;
    }

    public FormProgresss startedOn(Instant startedOn) {
        this.setStartedOn(startedOn);
        return this;
    }

    public void setStartedOn(Instant startedOn) {
        this.startedOn = startedOn;
    }

    public Instant getSubmitedOn() {
        return this.submitedOn;
    }

    public FormProgresss submitedOn(Instant submitedOn) {
        this.setSubmitedOn(submitedOn);
        return this;
    }

    public void setSubmitedOn(Instant submitedOn) {
        this.submitedOn = submitedOn;
    }

    public Instant getSentedOn() {
        return this.sentedOn;
    }

    public FormProgresss sentedOn(Instant sentedOn) {
        this.setSentedOn(sentedOn);
        return this;
    }

    public void setSentedOn(Instant sentedOn) {
        this.sentedOn = sentedOn;
    }

    public Set<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<Answer> answers) {
        if (this.answers != null) {
            this.answers.forEach(i -> i.setFormProgresss(null));
        }
        if (answers != null) {
            answers.forEach(i -> i.setFormProgresss(this));
        }
        this.answers = answers;
    }

    public FormProgresss answers(Set<Answer> answers) {
        this.setAnswers(answers);
        return this;
    }

    public FormProgresss addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setFormProgresss(this);
        return this;
    }

    public FormProgresss removeAnswer(Answer answer) {
        this.answers.remove(answer);
        answer.setFormProgresss(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FormProgresss user(User user) {
        this.setUser(user);
        return this;
    }

    public Form getForm() {
        return this.form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public FormProgresss form(Form form) {
        this.setForm(form);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormProgresss)) {
            return false;
        }
        return id != null && id.equals(((FormProgresss) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormProgresss{" +
            "id=" + getId() +
            ", submited='" + getSubmited() + "'" +
            ", startedOn='" + getStartedOn() + "'" +
            ", submitedOn='" + getSubmitedOn() + "'" +
            ", sentedOn='" + getSentedOn() + "'" +
            "}";
    }
}
