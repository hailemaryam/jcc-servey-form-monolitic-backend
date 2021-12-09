package et.com.hmmk.rmt.service.dto;

import java.io.Serializable;

public class FormProgressExtendedDTO implements Serializable {

    private FormProgresssDTO formProgresssDTO;

    private CompanyDTO companyDTO;

    public FormProgresssDTO getFormProgresssDTO() {
        return formProgresssDTO;
    }

    public void setFormProgresssDTO(FormProgresssDTO formProgresssDTO) {
        this.formProgresssDTO = formProgresssDTO;
    }

    public CompanyDTO getCompanyDTO() {
        return companyDTO;
    }

    public void setCompanyDTO(CompanyDTO companyDTO) {
        this.companyDTO = companyDTO;
    }
}
