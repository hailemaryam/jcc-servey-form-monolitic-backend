package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.Answer;
import et.com.hmmk.rmt.service.dto.AnswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, QuestionMapper.class, FormProgresssMapper.class })
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "question", source = "question", qualifiedByName = "title")
    @Mapping(target = "formProgresss", source = "formProgresss", qualifiedByName = "id")
    AnswerDTO toDto(Answer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnswerDTO toDtoId(Answer answer);
}
