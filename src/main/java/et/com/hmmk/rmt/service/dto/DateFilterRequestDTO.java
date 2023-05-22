package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.time.Instant;

public class DateFilterRequestDTO implements Serializable {

    private Instant projectStartDate;
    private Instant projectEndDate;
    private String region;

    public DateFilterRequestDTO() {}

    public DateFilterRequestDTO(Instant projectStartDate, Instant projectEndDate, String region) {
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.region = region;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
