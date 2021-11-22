package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link et.com.hmmk.rmt.domain.TypeOfOrganization} entity.
 */
public class TypeOfOrganizationDTO implements Serializable {

    private Long id;

    private String name;

    @Lob
    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfOrganizationDTO)) {
            return false;
        }

        TypeOfOrganizationDTO typeOfOrganizationDTO = (TypeOfOrganizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeOfOrganizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOfOrganizationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
