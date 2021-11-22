package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.Form;
import et.com.hmmk.rmt.service.dto.FormDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Form} and its DTO {@link FormDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface FormMapper extends EntityMapper<FormDTO, Form> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    FormDTO toDto(Form s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FormDTO toDtoName(Form form);
}
