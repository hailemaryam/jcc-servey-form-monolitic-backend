package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link et.com.hmmk.rmt.domain.MultipleChoiceAnsewer} entity.
 */
public class MultipleChoiceAnsewerDTO implements Serializable {

    private Long id;

    private String choice;

    private AnswerDTO answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public AnswerDTO getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDTO answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MultipleChoiceAnsewerDTO)) {
            return false;
        }

        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO = (MultipleChoiceAnsewerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, multipleChoiceAnsewerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MultipleChoiceAnsewerDTO{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            ", answer=" + getAnswer() +
            "}";
    }
}
