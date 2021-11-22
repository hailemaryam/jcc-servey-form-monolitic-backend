package et.com.hmmk.rmt.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultipleChoiceAnsewerMapperTest {

    private MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper;

    @BeforeEach
    public void setUp() {
        multipleChoiceAnsewerMapper = new MultipleChoiceAnsewerMapperImpl();
    }
}
