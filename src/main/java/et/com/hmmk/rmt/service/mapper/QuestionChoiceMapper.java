package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.QuestionChoice;
import et.com.hmmk.rmt.service.dto.QuestionChoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionChoice} and its DTO {@link QuestionChoiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface QuestionChoiceMapper extends EntityMapper<QuestionChoiceDTO, QuestionChoice> {
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    QuestionChoiceDTO toDto(QuestionChoice s);
}
