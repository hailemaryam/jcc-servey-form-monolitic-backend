package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.time.Instant;

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
    private Double numberOfMaleBeneficiary;
    private Double numberOfFemaleBeneficiary;
    private Instant projectStartDate;
    private Instant projectEndDate;
    private String geographicalFocus;

    public ExportByFormListResponseDTO() {}

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

    public Double getNumberOfMaleBeneficiary() {
        return numberOfMaleBeneficiary;
    }

    public void setNumberOfMaleBeneficiary(Double numberOfMaleBeneficiary) {
        this.numberOfMaleBeneficiary = numberOfMaleBeneficiary;
    }

    public Double getNumberOfFemaleBeneficiary() {
        return numberOfFemaleBeneficiary;
    }

    public void setNumberOfFemaleBeneficiary(Double numberOfFemaleBeneficiary) {
        this.numberOfFemaleBeneficiary = numberOfFemaleBeneficiary;
    }

    public Instant getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Instant projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Instant getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Instant projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getGeographicalFocus() {
        return geographicalFocus;
    }

    public void setGeographicalFocus(String geographicalFocus) {
        this.geographicalFocus = geographicalFocus;
    }
}
