package et.com.hmmk.rmt.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MultipleChoiceAnsewerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MultipleChoiceAnsewerDTO.class);
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO1 = new MultipleChoiceAnsewerDTO();
        multipleChoiceAnsewerDTO1.setId(1L);
        MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO2 = new MultipleChoiceAnsewerDTO();
        assertThat(multipleChoiceAnsewerDTO1).isNotEqualTo(multipleChoiceAnsewerDTO2);
        multipleChoiceAnsewerDTO2.setId(multipleChoiceAnsewerDTO1.getId());
        assertThat(multipleChoiceAnsewerDTO1).isEqualTo(multipleChoiceAnsewerDTO2);
        multipleChoiceAnsewerDTO2.setId(2L);
        assertThat(multipleChoiceAnsewerDTO1).isNotEqualTo(multipleChoiceAnsewerDTO2);
        multipleChoiceAnsewerDTO1.setId(null);
        assertThat(multipleChoiceAnsewerDTO1).isNotEqualTo(multipleChoiceAnsewerDTO2);
    }
}
