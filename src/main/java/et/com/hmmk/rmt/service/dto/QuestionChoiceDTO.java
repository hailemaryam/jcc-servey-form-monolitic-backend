package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link et.com.hmmk.rmt.domain.QuestionChoice} entity.
 */
public class QuestionChoiceDTO implements Serializable {

    private Long id;

    private String option;

    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionChoiceDTO)) {
            return false;
        }

        QuestionChoiceDTO questionChoiceDTO = (QuestionChoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionChoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionChoiceDTO{" +
            "id=" + getId() +
            ", option='" + getOption() + "'" +
            ", question=" + getQuestion() +
            "}";
    }
}
