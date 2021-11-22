package et.com.hmmk.rmt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfOrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfOrganization.class);
        TypeOfOrganization typeOfOrganization1 = new TypeOfOrganization();
        typeOfOrganization1.setId(1L);
        TypeOfOrganization typeOfOrganization2 = new TypeOfOrganization();
        typeOfOrganization2.setId(typeOfOrganization1.getId());
        assertThat(typeOfOrganization1).isEqualTo(typeOfOrganization2);
        typeOfOrganization2.setId(2L);
        assertThat(typeOfOrganization1).isNotEqualTo(typeOfOrganization2);
        typeOfOrganization1.setId(null);
        assertThat(typeOfOrganization1).isNotEqualTo(typeOfOrganization2);
    }
}
