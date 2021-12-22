package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;

public class ExportByFormListResponseDTO implements Serializable {

    private Long formProgressId;
    private String projectName;
    private String projectDescription;

    private String organizationName;
    private String organizationType;

    private String objectiveOfTheProject;
    private Double totalCommittedFund;
    private Double dispersedIn;
    private String sectoralScope;
    private Double numberOfBeneficiary;
    private String geographicalFocus;

    public Long getFormProgressId() {
        return formProgressId;
    }

    public void setFormProgressId(Long formProgressId) {
        this.formProgressId = formProgressId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getObjectiveOfTheProject() {
        return objectiveOfTheProject;
    }

    public void setObjectiveOfTheProject(String objectiveOfTheProject) {
        this.objectiveOfTheProject = objectiveOfTheProject;
    }

    public Double getTotalCommittedFund() {
        return totalCommittedFund;
    }

    public void setTotalCommittedFund(Double totalCommittedFund) {
        this.totalCommittedFund = totalCommittedFund;
    }

    public Double getDispersedIn() {
        return dispersedIn;
    }

    public void setDispersedIn(Double dispersedIn) {
        this.dispersedIn = dispersedIn;
    }

    public String getSectoralScope() {
        return sectoralScope;
    }

    public void setSectoralScope(String sectoralScope) {
        this.sectoralScope = sectoralScope;
    }

    public Double getNumberOfBeneficiary() {
        return numberOfBeneficiary;
    }

    public void setNumberOfBeneficiary(Double numberOfBeneficiary) {
        this.numberOfBeneficiary = numberOfBeneficiary;
    }

    public String getGeographicalFocus() {
        return geographicalFocus;
    }

    public void setGeographicalFocus(String geographicalFocus) {
        this.geographicalFocus = geographicalFocus;
    }
}
