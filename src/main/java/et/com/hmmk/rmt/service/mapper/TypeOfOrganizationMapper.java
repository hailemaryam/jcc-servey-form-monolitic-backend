package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.TypeOfOrganization;
import et.com.hmmk.rmt.service.dto.TypeOfOrganizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeOfOrganization} and its DTO {@link TypeOfOrganizationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeOfOrganizationMapper extends EntityMapper<TypeOfOrganizationDTO, TypeOfOrganization> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TypeOfOrganizationDTO toDtoName(TypeOfOrganization typeOfOrganization);
}
