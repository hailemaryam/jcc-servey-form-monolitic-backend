package et.com.hmmk.rmt.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeOfOrganizationMapperTest {

    private TypeOfOrganizationMapper typeOfOrganizationMapper;

    @BeforeEach
    public void setUp() {
        typeOfOrganizationMapper = new TypeOfOrganizationMapperImpl();
    }
}
