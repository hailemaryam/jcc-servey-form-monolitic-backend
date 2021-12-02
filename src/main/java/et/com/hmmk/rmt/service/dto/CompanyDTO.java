package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link et.com.hmmk.rmt.domain.Company} entity.
 */
public class CompanyDTO implements Serializable {

    private Long id;

    private String companyName;

    private String strategicObjective;

    private String futureFocusArea;

    private String currentFundingCycle;

    private UserDTO user;

    private TypeOfOrganizationDTO typeOfOrganation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStrategicObjective() {
        return strategicObjective;
    }

    public void setStrategicObjective(String strategicObjective) {
        this.strategicObjective = strategicObjective;
    }

    public String getFutureFocusArea() {
        return futureFocusArea;
    }

    public void setFutureFocusArea(String futureFocusArea) {
        this.futureFocusArea = futureFocusArea;
    }

    public String getCurrentFundingCycle() {
        return currentFundingCycle;
    }

    public void setCurrentFundingCycle(String currentFundingCycle) {
        this.currentFundingCycle = currentFundingCycle;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public TypeOfOrganizationDTO getTypeOfOrganation() {
        return typeOfOrganation;
    }

    public void setTypeOfOrganation(TypeOfOrganizationDTO typeOfOrganation) {
        this.typeOfOrganation = typeOfOrganation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyDTO)) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", strategicObjective='" + getStrategicObjective() + "'" +
            ", futureFocusArea='" + getFutureFocusArea() + "'" +
            ", currentFundingCycle='" + getCurrentFundingCycle() + "'" +
            ", user=" + getUser() +
            ", typeOfOrganation=" + getTypeOfOrganation() +
            "}";
    }
}
