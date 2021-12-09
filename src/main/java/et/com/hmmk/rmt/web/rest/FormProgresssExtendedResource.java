package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.repository.FormProgresssRepository;
import et.com.hmmk.rmt.service.CompanyService;
import et.com.hmmk.rmt.service.FormProgresssQueryService;
import et.com.hmmk.rmt.service.FormProgresssService;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.dto.FormProgressExtendedDTO;
import et.com.hmmk.rmt.service.dto.FormProgresssDTO;
import et.com.hmmk.rmt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link et.com.hmmk.rmt.domain.FormProgresss}.
 */
@RestController
@RequestMapping("/api")
public class FormProgresssExtendedResource {

    private final Logger log = LoggerFactory.getLogger(FormProgresssExtendedResource.class);

    private static final String ENTITY_NAME = "formProgresss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormProgresssService formProgresssService;

    private final FormProgresssQueryService formProgresssQueryService;

    private final CompanyService companyService;

    public FormProgresssExtendedResource(
        FormProgresssService formProgresssService,
        FormProgresssQueryService formProgresssQueryService,
        CompanyService companyService
    ) {
        this.formProgresssService = formProgresssService;
        this.formProgresssQueryService = formProgresssQueryService;
        this.companyService = companyService;
    }

    @GetMapping("/form-progress-extended")
    public ResponseEntity<List<FormProgressExtendedDTO>> getAllFormProgressses(FormProgresssCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FormProgressses by criteria: {}", criteria);
        Page<FormProgresssDTO> page = formProgresssQueryService.findByCriteria(criteria, pageable);
        Page<FormProgressExtendedDTO> extendedDTO = page.map(formProgresssDTO -> toExtendFormProgress(formProgresssDTO));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(extendedDTO.getContent());
    }

    @GetMapping("/form-progress-extended/{id}")
    public ResponseEntity<FormProgressExtendedDTO> getFormProgresss(@PathVariable Long id) {
        log.debug("REST request to get FormProgresss : {}", id);
        Optional<FormProgresssDTO> formProgresssDTO = formProgresssService.findOne(id);
        return ResponseEntity.ok().body(toExtendFormProgress(formProgresssDTO.get()));
    }

    private FormProgressExtendedDTO toExtendFormProgress(FormProgresssDTO formProgresssDTO) {
        FormProgressExtendedDTO formProgressExtendedDTO = new FormProgressExtendedDTO();
        formProgressExtendedDTO.setFormProgresssDTO(formProgresssDTO);
        formProgressExtendedDTO.setCompanyDTO(companyService.findOne(formProgresssDTO.getUser().getId()).get());
        return formProgressExtendedDTO;
    }
}
