package et.com.hmmk.rmt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import et.com.hmmk.rmt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormProgresssTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormProgresss.class);
        FormProgresss formProgresss1 = new FormProgresss();
        formProgresss1.setId(1L);
        FormProgresss formProgresss2 = new FormProgresss();
        formProgresss2.setId(formProgresss1.getId());
        assertThat(formProgresss1).isEqualTo(formProgresss2);
        formProgresss2.setId(2L);
        assertThat(formProgresss1).isNotEqualTo(formProgresss2);
        formProgresss1.setId(null);
        assertThat(formProgresss1).isNotEqualTo(formProgresss2);
    }
}
