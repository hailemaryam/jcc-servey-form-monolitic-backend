package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.MultipleChoiceAnsewer;
import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MultipleChoiceAnsewer} and its DTO {@link MultipleChoiceAnsewerDTO}.
 */
@Mapper(componentModel = "spring", uses = { AnswerMapper.class })
public interface MultipleChoiceAnsewerMapper extends EntityMapper<MultipleChoiceAnsewerDTO, MultipleChoiceAnsewer> {
    @Mapping(target = "answer", source = "answer", qualifiedByName = "id")
    MultipleChoiceAnsewerDTO toDto(MultipleChoiceAnsewer s);
}
