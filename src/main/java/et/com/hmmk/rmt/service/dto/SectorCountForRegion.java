package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.Map;

public class SectorCountForRegion implements Serializable {

    private Map<String, Long> sectoralScopeCount;
    private String region;

    public SectorCountForRegion() {}

    public Map<String, Long> getSectoralScopeCount() {
        return sectoralScopeCount;
    }

    public SectorCountForRegion(Map<String, Long> sectoralScopeCount, String region) {
        this.sectoralScopeCount = sectoralScopeCount;
        this.region = region;
    }

    public void setSectoralScopeCount(Map<String, Long> sectoralScopeCount) {
        this.sectoralScopeCount = sectoralScopeCount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
