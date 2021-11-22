package et.com.hmmk.rmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MultipleChoiceAnsewer.
 */
@Entity
@Table(name = "multiple_choice_ansewer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MultipleChoiceAnsewer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "choice")
    private String choice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "multipleChoiceAnsewers", "user", "question", "formProgresss" }, allowSetters = true)
    private Answer answer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MultipleChoiceAnsewer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChoice() {
        return this.choice;
    }

    public MultipleChoiceAnsewer choice(String choice) {
        this.setChoice(choice);
        return this;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Answer getAnswer() {
        return this.answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public MultipleChoiceAnsewer answer(Answer answer) {
        this.setAnswer(answer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MultipleChoiceAnsewer)) {
            return false;
        }
        return id != null && id.equals(((MultipleChoiceAnsewer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MultipleChoiceAnsewer{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            "}";
    }
}
