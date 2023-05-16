package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.service.*;
import et.com.hmmk.rmt.service.criteria.AnswerCriteria;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.criteria.MultipleChoiceAnsewerCriteria;
import et.com.hmmk.rmt.service.dto.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.service.filter.LongFilter;

/**
 * REST controller for managing {@link et.com.hmmk.rmt.domain.Form}.
 */
@RestController
@RequestMapping("/api")
public class ExportResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerQueryService answerQueryService;

    private final MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService;

    private final FormProgresssQueryService formProgresssQueryService;

    private final CompanyService companyService;

    private final ProjectService projectService;

    public ExportResource(
        AnswerQueryService answerQueryService,
        MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService,
        FormProgresssQueryService formProgresssQueryService,
        CompanyService companyService,
        ProjectService projectService
    ) {
        this.answerQueryService = answerQueryService;
        this.multipleChoiceAnsewerQueryService = multipleChoiceAnsewerQueryService;
        this.formProgresssQueryService = formProgresssQueryService;
        this.companyService = companyService;
        this.projectService = projectService;
    }

    @PostMapping("/exportByFormId")
    public List<ExportByFormListResponseDTO> exportByFormId(@RequestBody ExportByFormListRequestDTO exportByFormListRequestDTO)
        throws URISyntaxException {
        List<ExportByFormListResponseDTO> exportByFormListResponseDTOList = prepareData(exportByFormListRequestDTO);
        return exportByFormListResponseDTOList;
    }

    private List<ExportByFormListResponseDTO> prepareData(ExportByFormListRequestDTO exportByFormListRequestDTO) {
        List<ExportByFormListResponseDTO> responseDTOList = new ArrayList<>();
        for (FormProgresssDTO formProgresssDTO : getFormProgressByFormId(exportByFormListRequestDTO)) {
            ExportByFormListResponseDTO exportByFormListResponseDTO = new ExportByFormListResponseDTO();
            exportByFormListResponseDTO.setFormProgressId(formProgresssDTO.getId());
            companyService
                .findOne(formProgresssDTO.getUser().getId())
                .ifPresent(companyDTO -> {
                    exportByFormListResponseDTO.setOrganizationName(companyDTO.getCompanyName());
                    exportByFormListResponseDTO.setOrganizationType(companyDTO.getTypeOfOrganation().getName());
                });
            projectService
                .findOne(formProgresssDTO.getProject().getId())
                .ifPresent(projectDTO -> {
                    exportByFormListResponseDTO.setProjectDescription(projectDTO.getProjectDescription());
                    exportByFormListResponseDTO.setProjectName(projectDTO.getProjectName());
                });
            int i = 1;
            for (AnswerDTO answersByFormProgress : getAnswersByFormProgress(formProgresssDTO.getId())) {
                if (i % 9 == 1) {
                    MultipleChoiceAnsewerCriteria multipleChoiceAnsewerCriteria = new MultipleChoiceAnsewerCriteria();
                    LongFilter answerId = new LongFilter();
                    answerId.setEquals(answersByFormProgress.getId());
                    multipleChoiceAnsewerCriteria.setAnswerId(answerId);
                    String objectiveOfProject = "";
                    for (MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO : multipleChoiceAnsewerQueryService.findByCriteria(
                        multipleChoiceAnsewerCriteria
                    )) {
                        if (objectiveOfProject.equals("")) {
                            objectiveOfProject = multipleChoiceAnsewerDTO.getChoice();
                        } else {
                            objectiveOfProject = objectiveOfProject + "| " + multipleChoiceAnsewerDTO.getChoice();
                        }
                    }
                    exportByFormListResponseDTO.setObjectiveOfTheProject(objectiveOfProject);
                } else if (i % 9 == 2) {
                    exportByFormListResponseDTO.setTotalCommittedFund(answersByFormProgress.getNumber());
                } else if (i % 9 == 3) {
                    exportByFormListResponseDTO.setDispersedIn(answersByFormProgress.getNumber());
                } else if (i % 9 == 4) {
                    exportByFormListResponseDTO.setSectoralScope(answersByFormProgress.getShortAnswer());
                } else if (i % 9 == 5) {
                    exportByFormListResponseDTO.setNumberOfMaleBeneficiary(answersByFormProgress.getNumber());
                } else if (i % 9 == 6) {
                    exportByFormListResponseDTO.setNumberOfFemaleBeneficiary(answersByFormProgress.getNumber());
                } else if (i % 9 == 7) {
                    exportByFormListResponseDTO.setProjectStartDate(answersByFormProgress.getDate());
                } else if (i % 9 == 8) {
                    exportByFormListResponseDTO.setProjectEndDate(answersByFormProgress.getDate());
                } else if (i % 9 == 0) {
                    MultipleChoiceAnsewerCriteria multipleChoiceAnsewerCriteria = new MultipleChoiceAnsewerCriteria();
                    LongFilter answerId = new LongFilter();
                    answerId.setEquals(answersByFormProgress.getId());
                    multipleChoiceAnsewerCriteria.setAnswerId(answerId);
                    String geographicalFocusArea = "";
                    for (MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO : multipleChoiceAnsewerQueryService.findByCriteria(
                        multipleChoiceAnsewerCriteria
                    )) {
                        if (geographicalFocusArea.equals("")) {
                            geographicalFocusArea = multipleChoiceAnsewerDTO.getChoice();
                        } else {
                            geographicalFocusArea = geographicalFocusArea + "| " + multipleChoiceAnsewerDTO.getChoice();
                        }
                    }
                    exportByFormListResponseDTO.setGeographicalFocus(geographicalFocusArea);
                }
                i++;
            }
            responseDTOList.add(exportByFormListResponseDTO);
        }
        return responseDTOList;
    }

    private List<AnswerDTO> getAnswersByFormProgress(Long formProgressId) {
        AnswerCriteria answerCriteria = new AnswerCriteria();
        LongFilter formProgressIds = new LongFilter();
        formProgressIds.setEquals(formProgressId);
        answerCriteria.setFormProgresssId(formProgressIds);
        return answerQueryService.findByCriteria(answerCriteria);
    }

    private List<FormProgresssDTO> getFormProgressByFormId(ExportByFormListRequestDTO exportByFormListRequestDTO) {
        FormProgresssCriteria formProgresssCriteria = new FormProgresssCriteria();
        LongFilter formIds = new LongFilter();
        formIds.setIn(exportByFormListRequestDTO.getFormIdList());
        formProgresssCriteria.setFormId(formIds);
        return formProgresssQueryService.findByCriteria(formProgresssCriteria);
    }
}
