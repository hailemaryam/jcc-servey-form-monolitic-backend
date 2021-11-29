package et.com.hmmk.rmt.service.dto;

import et.com.hmmk.rmt.domain.enumeration.DataType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link et.com.hmmk.rmt.domain.Question} entity.
 */
public class QuestionCreatorDTO implements Serializable {

    private Long id;

    @Lob
    private String title;

    @NotNull
    private Boolean mandatory;

    private DataType dataType;

    private FormDTO form;

    private Set<QuestionChoiceDTO> questionChoices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public Set<QuestionChoiceDTO> getQuestionChoices() {
        return questionChoices;
    }

    public void setQuestionChoices(Set<QuestionChoiceDTO> questionChoices) {
        this.questionChoices = questionChoices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionCreatorDTO)) {
            return false;
        }

        QuestionCreatorDTO questionDTO = (QuestionCreatorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", mandatory='" + getMandatory() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", form=" + getForm() +
            "}";
    }
}
