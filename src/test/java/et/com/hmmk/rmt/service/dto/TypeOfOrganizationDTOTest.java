package et.com.hmmk.rmt.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfOrganizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfOrganizationDTO.class);
        TypeOfOrganizationDTO typeOfOrganizationDTO1 = new TypeOfOrganizationDTO();
        typeOfOrganizationDTO1.setId(1L);
        TypeOfOrganizationDTO typeOfOrganizationDTO2 = new TypeOfOrganizationDTO();
        assertThat(typeOfOrganizationDTO1).isNotEqualTo(typeOfOrganizationDTO2);
        typeOfOrganizationDTO2.setId(typeOfOrganizationDTO1.getId());
        assertThat(typeOfOrganizationDTO1).isEqualTo(typeOfOrganizationDTO2);
        typeOfOrganizationDTO2.setId(2L);
        assertThat(typeOfOrganizationDTO1).isNotEqualTo(typeOfOrganizationDTO2);
        typeOfOrganizationDTO1.setId(null);
        assertThat(typeOfOrganizationDTO1).isNotEqualTo(typeOfOrganizationDTO2);
    }
}
