package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.List;

public class RegionWithSectorList implements Serializable {

    private String region;
    private List<String> sector;

    public RegionWithSectorList() {}

    public RegionWithSectorList(String region, List<String> sector) {
        this.region = region;
        this.sector = sector;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getSector() {
        return sector;
    }

    public void setSector(List<String> sector) {
        this.sector = sector;
    }
}
