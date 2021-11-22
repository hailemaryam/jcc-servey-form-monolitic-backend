package et.com.hmmk.rmt.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionChoiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionChoiceDTO.class);
        QuestionChoiceDTO questionChoiceDTO1 = new QuestionChoiceDTO();
        questionChoiceDTO1.setId(1L);
        QuestionChoiceDTO questionChoiceDTO2 = new QuestionChoiceDTO();
        assertThat(questionChoiceDTO1).isNotEqualTo(questionChoiceDTO2);
        questionChoiceDTO2.setId(questionChoiceDTO1.getId());
        assertThat(questionChoiceDTO1).isEqualTo(questionChoiceDTO2);
        questionChoiceDTO2.setId(2L);
        assertThat(questionChoiceDTO1).isNotEqualTo(questionChoiceDTO2);
        questionChoiceDTO1.setId(null);
        assertThat(questionChoiceDTO1).isNotEqualTo(questionChoiceDTO2);
    }
}
