package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class SectorWithRegion implements Serializable {

    private Instant projectStartDate;
    private Instant projectEndDate;

    private String sector;
    private List<String> region;

    public SectorWithRegion() {}

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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public List<String> getRegion() {
        return region;
    }

    public void setRegion(List<String> region) {
        this.region = region;
    }
}
