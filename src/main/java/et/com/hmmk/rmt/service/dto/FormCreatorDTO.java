package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link et.com.hmmk.rmt.domain.Form} entity.
 */
public class FormCreatorDTO implements Serializable {

    private Long id;

    private String name;

    @Lob
    private String description;

    private Instant createdOn;

    private Instant updatedOn;

    private UserDTO user;

    private Set<QuestionCreatorDTO> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<QuestionCreatorDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionCreatorDTO> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormCreatorDTO)) {
            return false;
        }

        FormCreatorDTO formDTO = (FormCreatorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
