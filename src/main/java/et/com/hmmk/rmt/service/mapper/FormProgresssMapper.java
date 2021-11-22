package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.FormProgresss;
import et.com.hmmk.rmt.service.dto.FormProgresssDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormProgresss} and its DTO {@link FormProgresssDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, FormMapper.class })
public interface FormProgresssMapper extends EntityMapper<FormProgresssDTO, FormProgresss> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "form", source = "form", qualifiedByName = "name")
    FormProgresssDTO toDto(FormProgresss s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormProgresssDTO toDtoId(FormProgresss formProgresss);
}
