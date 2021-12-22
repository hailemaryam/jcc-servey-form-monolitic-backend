package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO representing a user, with his authorities.
 */
public class ExportByFormListRequestDTO implements Serializable {

    private List<Long> formIdList;

    public List<Long> getFormIdList() {
        return formIdList;
    }

    public void setFormIdList(List<Long> formIdList) {
        this.formIdList = formIdList;
    }
}
