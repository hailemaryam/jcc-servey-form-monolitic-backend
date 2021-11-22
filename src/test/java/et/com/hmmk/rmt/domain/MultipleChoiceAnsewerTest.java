package et.com.hmmk.rmt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MultipleChoiceAnsewerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MultipleChoiceAnsewer.class);
        MultipleChoiceAnsewer multipleChoiceAnsewer1 = new MultipleChoiceAnsewer();
        multipleChoiceAnsewer1.setId(1L);
        MultipleChoiceAnsewer multipleChoiceAnsewer2 = new MultipleChoiceAnsewer();
        multipleChoiceAnsewer2.setId(multipleChoiceAnsewer1.getId());
        assertThat(multipleChoiceAnsewer1).isEqualTo(multipleChoiceAnsewer2);
        multipleChoiceAnsewer2.setId(2L);
        assertThat(multipleChoiceAnsewer1).isNotEqualTo(multipleChoiceAnsewer2);
        multipleChoiceAnsewer1.setId(null);
        assertThat(multipleChoiceAnsewer1).isNotEqualTo(multipleChoiceAnsewer2);
    }
}
