package et.com.hmmk.rmt.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionChoiceMapperTest {

    private QuestionChoiceMapper questionChoiceMapper;

    @BeforeEach
    public void setUp() {
        questionChoiceMapper = new QuestionChoiceMapperImpl();
    }
}
