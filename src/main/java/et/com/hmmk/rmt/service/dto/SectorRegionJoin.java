package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.List;

public class SectorRegionJoin implements Serializable {

    private String sector;
    private String region;

    public SectorRegionJoin() {}

    public SectorRegionJoin(String sector, String region) {
        this.sector = sector;
        this.region = region;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
