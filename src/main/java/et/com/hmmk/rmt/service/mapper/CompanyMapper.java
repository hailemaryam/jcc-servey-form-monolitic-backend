package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.Company;
import et.com.hmmk.rmt.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, TypeOfOrganizationMapper.class })
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "typeOfOrganation", source = "typeOfOrganation", qualifiedByName = "name")
    CompanyDTO toDto(Company s);

    @Named("companyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "companyName", source = "companyName")
    CompanyDTO toDtoCompanyName(Company company);
}
