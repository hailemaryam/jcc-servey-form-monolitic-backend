package et.com.hmmk.rmt.service.mapper;

import et.com.hmmk.rmt.domain.News;
import et.com.hmmk.rmt.service.dto.NewsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link News} and its DTO {@link NewsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NewsMapper extends EntityMapper<NewsDTO, News> {}
