package et.com.hmmk.rmt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionChoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionChoice.class);
        QuestionChoice questionChoice1 = new QuestionChoice();
        questionChoice1.setId(1L);
        QuestionChoice questionChoice2 = new QuestionChoice();
        questionChoice2.setId(questionChoice1.getId());
        assertThat(questionChoice1).isEqualTo(questionChoice2);
        questionChoice2.setId(2L);
        assertThat(questionChoice1).isNotEqualTo(questionChoice2);
        questionChoice1.setId(null);
        assertThat(questionChoice1).isNotEqualTo(questionChoice2);
    }
}
