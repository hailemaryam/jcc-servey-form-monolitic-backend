package et.com.hmmk.rmt.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormProgresssDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormProgresssDTO.class);
        FormProgresssDTO formProgresssDTO1 = new FormProgresssDTO();
        formProgresssDTO1.setId(1L);
        FormProgresssDTO formProgresssDTO2 = new FormProgresssDTO();
        assertThat(formProgresssDTO1).isNotEqualTo(formProgresssDTO2);
        formProgresssDTO2.setId(formProgresssDTO1.getId());
        assertThat(formProgresssDTO1).isEqualTo(formProgresssDTO2);
        formProgresssDTO2.setId(2L);
        assertThat(formProgresssDTO1).isNotEqualTo(formProgresssDTO2);
        formProgresssDTO1.setId(null);
        assertThat(formProgresssDTO1).isNotEqualTo(formProgresssDTO2);
    }
}
