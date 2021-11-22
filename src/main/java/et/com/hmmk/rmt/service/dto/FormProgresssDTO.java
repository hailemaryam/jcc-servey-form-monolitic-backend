package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link et.com.hmmk.rmt.domain.FormProgresss} entity.
 */
public class FormProgresssDTO implements Serializable {

    private Long id;

    private Boolean submited;

    private Instant startedOn;

    private Instant submitedOn;

    private Instant sentedOn;

    private UserDTO user;

    private FormDTO form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSubmited() {
        return submited;
    }

    public void setSubmited(Boolean submited) {
        this.submited = submited;
    }

    public Instant getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Instant startedOn) {
        this.startedOn = startedOn;
    }

    public Instant getSubmitedOn() {
        return submitedOn;
    }

    public void setSubmitedOn(Instant submitedOn) {
        this.submitedOn = submitedOn;
    }

    public Instant getSentedOn() {
        return sentedOn;
    }

    public void setSentedOn(Instant sentedOn) {
        this.sentedOn = sentedOn;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormProgresssDTO)) {
            return false;
        }

        FormProgresssDTO formProgresssDTO = (FormProgresssDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formProgresssDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormProgresssDTO{" +
            "id=" + getId() +
            ", submited='" + getSubmited() + "'" +
            ", startedOn='" + getStartedOn() + "'" +
            ", submitedOn='" + getSubmitedOn() + "'" +
            ", sentedOn='" + getSentedOn() + "'" +
            ", user=" + getUser() +
            ", form=" + getForm() +
            "}";
    }
}
