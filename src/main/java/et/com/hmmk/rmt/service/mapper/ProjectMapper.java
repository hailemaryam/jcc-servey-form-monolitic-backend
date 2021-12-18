package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.Project;
import et.com.hmmk.rmt.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companyName")
    ProjectDTO toDto(Project s);

    @Named("projectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "projectName", source = "projectName")
    ProjectDTO toDtoProjectName(Project project);
}
