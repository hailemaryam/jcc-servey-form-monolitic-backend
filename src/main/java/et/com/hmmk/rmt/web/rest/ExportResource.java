package et.com.hmmk.rmt.web.rest;

import et.com.hmmk.rmt.service.*;
import et.com.hmmk.rmt.service.criteria.AnswerCriteria;
import et.com.hmmk.rmt.service.criteria.FormProgresssCriteria;
import et.com.hmmk.rmt.service.criteria.MultipleChoiceAnsewerCriteria;
import et.com.hmmk.rmt.service.dto.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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

    private final FormProgresssService formProgresssService;

    private final CompanyService companyService;

    private final ProjectService projectService;

    public ExportResource(
        AnswerQueryService answerQueryService,
        MultipleChoiceAnsewerQueryService multipleChoiceAnsewerQueryService,
        FormProgresssQueryService formProgresssQueryService,
        FormProgresssService formProgresssService,
        CompanyService companyService,
        ProjectService projectService
    ) {
        this.answerQueryService = answerQueryService;
        this.multipleChoiceAnsewerQueryService = multipleChoiceAnsewerQueryService;
        this.formProgresssQueryService = formProgresssQueryService;
        this.formProgresssService = formProgresssService;
        this.companyService = companyService;
        this.projectService = projectService;
    }

    @PostMapping("/exportByFormId")
    public List<ExportByFormListResponseDTO> exportByFormId(@RequestBody ExportByFormListRequestDTO exportByFormListRequestDTO)
        throws URISyntaxException {
        List<ExportByFormListResponseDTO> exportByFormListResponseDTOList = prepareData(exportByFormListRequestDTO);
        return exportByFormListResponseDTOList;
    }

    @PostMapping("/groupedBySectorValue")
    public List<ExportByFormListResponseDTO> groupedBySectorValue(@RequestBody DateFilterRequestDTO dateFilterRequestDTO)
        throws URISyntaxException {
        List<ExportByFormListResponseDTO> exportByFormListResponseDTOList = prepareGroupBySector(dateFilterRequestDTO);
        return exportByFormListResponseDTOList;
    }

    @PostMapping("/getRegionalSectorCount")
    public List<SectorCountForRegion> getRegionalSectorCount(@RequestBody DateFilterRequestDTO dateFilterRequestDTO)
        throws URISyntaxException {
        List<SectorCountForRegion> exportByFormListResponseDTOList = prepareRegionalSectorCount(dateFilterRequestDTO);
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
            extractAndSetAnswer(formProgresssDTO, exportByFormListResponseDTO);
            responseDTOList.add(exportByFormListResponseDTO);
        }
        return responseDTOList;
    }

    private List<ExportByFormListResponseDTO> prepareGroupBySector(DateFilterRequestDTO dateFilterRequestDTO) {
        List<ExportByFormListResponseDTO> responseDTOList = new ArrayList<>();
        for (FormProgresssDTO formProgresssDTO : formProgresssService.findAll(Pageable.unpaged())) {
            ExportByFormListResponseDTO exportByFormListResponseDTO = new ExportByFormListResponseDTO();
            extractAndSetAnswer(formProgresssDTO, exportByFormListResponseDTO);
            responseDTOList.add(exportByFormListResponseDTO);
        }
        return responseDTOList
            .stream()
            .filter(item ->
                item.getProjectStartDate().isAfter(dateFilterRequestDTO.getProjectStartDate()) &&
                item.getProjectStartDate().isBefore(dateFilterRequestDTO.getProjectEndDate()) ||
                item.getProjectStartDate().isBefore(dateFilterRequestDTO.getProjectStartDate()) &&
                item.getProjectEndDate().isAfter(dateFilterRequestDTO.getProjectEndDate()) ||
                item.getProjectEndDate().isAfter(dateFilterRequestDTO.getProjectStartDate()) &&
                item.getProjectEndDate().isBefore(dateFilterRequestDTO.getProjectEndDate())
            )
            .collect(Collectors.toList())
            .stream()
            .collect(Collectors.groupingBy(ExportByFormListResponseDTO::getSectoralScope))
            .entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    x -> {
                        Double commitedFundSum = x
                            .getValue()
                            .stream()
                            .mapToDouble(ExportByFormListResponseDTO::getTotalCommittedFund)
                            .sum();
                        Double dispersedSum = x.getValue().stream().mapToDouble(ExportByFormListResponseDTO::getDispersedIn).sum();
                        Double maleBeneficiarySum = x
                            .getValue()
                            .stream()
                            .mapToDouble(ExportByFormListResponseDTO::getNumberOfMaleBeneficiary)
                            .sum();
                        Double femaleBeneficiarySum = x
                            .getValue()
                            .stream()
                            .mapToDouble(ExportByFormListResponseDTO::getNumberOfFemaleBeneficiary)
                            .sum();
                        return new ExportByFormListResponseDTO(
                            x.getKey(),
                            commitedFundSum,
                            dispersedSum,
                            maleBeneficiarySum,
                            femaleBeneficiarySum
                        );
                    },
                    Map.Entry::getValue
                )
            )
            .entrySet()
            .stream()
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private List<SectorCountForRegion> prepareRegionalSectorCount(DateFilterRequestDTO dateFilterRequestDTO) {
        List<SectorWithRegion> sectorWithRegionList = new ArrayList<>();
        for (FormProgresssDTO formProgresssDTO : formProgresssService.findAll(Pageable.unpaged())) {
            extractAndSetAnswerForRegionalSectorReport(formProgresssDTO, sectorWithRegionList);
        }
        List<SectorRegionJoin> sectorRegionJoinList = new ArrayList<>();
        List<SectorWithRegion> filteredWithDate = sectorWithRegionList
            .stream()
            .filter(item ->
                item.getProjectStartDate().isAfter(dateFilterRequestDTO.getProjectStartDate()) &&
                item.getProjectStartDate().isBefore(dateFilterRequestDTO.getProjectEndDate()) ||
                item.getProjectStartDate().isBefore(dateFilterRequestDTO.getProjectStartDate()) &&
                item.getProjectEndDate().isAfter(dateFilterRequestDTO.getProjectEndDate()) ||
                item.getProjectEndDate().isAfter(dateFilterRequestDTO.getProjectStartDate()) &&
                item.getProjectEndDate().isBefore(dateFilterRequestDTO.getProjectEndDate())
            )
            .collect(Collectors.toList());
        for (SectorWithRegion sectorWithRegion : filteredWithDate) {
            for (String s : sectorWithRegion.getRegion()) {
                sectorRegionJoinList.add(new SectorRegionJoin(sectorWithRegion.getSector(), s));
            }
        }
        return sectorRegionJoinList
            .stream()
            .collect(Collectors.groupingBy(SectorRegionJoin::getRegion))
            .entrySet()
            .stream()
            .map(x -> {
                List<String> sectorList = x.getValue().stream().map(s -> s.getSector()).collect(Collectors.toList());
                return new RegionWithSectorList(x.getKey(), sectorList);
            })
            .map(rs -> {
                Map<String, Long> collect = rs
                    .getSector()
                    .stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                return new SectorCountForRegion(collect, rs.getRegion());
            })
            .filter(item -> item.getRegion().equals(dateFilterRequestDTO.getRegion()))
            .collect(Collectors.toList());
    }

    private void extractAndSetAnswerForRegionalSectorReport(
        FormProgresssDTO formProgresssDTO,
        List<SectorWithRegion> sectorWithRegionList
    ) {
        SectorWithRegion exportByFormListResponseDTO = new SectorWithRegion();
        int i = 1;
        for (AnswerDTO answersByFormProgress : getAnswersByFormProgress(formProgresssDTO.getId())) {
            if (i % 9 == 4) {
                exportByFormListResponseDTO.setSector(answersByFormProgress.getShortAnswer());
            } else if (i % 9 == 7) {
                exportByFormListResponseDTO.setProjectStartDate(answersByFormProgress.getDate());
            } else if (i % 9 == 8) {
                exportByFormListResponseDTO.setProjectEndDate(answersByFormProgress.getDate());
            } else if (i % 9 == 0) {
                exportByFormListResponseDTO.setRegion(geographicalFocusAriaSetterForList(answersByFormProgress));
            }
            i++;
        }
        sectorWithRegionList.add(exportByFormListResponseDTO);
    }

    private List<String> geographicalFocusAriaSetterForList(AnswerDTO answersByFormProgress) {
        MultipleChoiceAnsewerCriteria multipleChoiceAnsewerCriteria = new MultipleChoiceAnsewerCriteria();
        LongFilter answerId = new LongFilter();
        answerId.setEquals(answersByFormProgress.getId());
        multipleChoiceAnsewerCriteria.setAnswerId(answerId);
        List<String> geographicalFocusArea = new ArrayList<>();
        for (MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO : multipleChoiceAnsewerQueryService.findByCriteria(
            multipleChoiceAnsewerCriteria
        )) {
            geographicalFocusArea.add(multipleChoiceAnsewerDTO.getChoice());
        }
        return geographicalFocusArea;
    }

    private void extractAndSetAnswer(FormProgresssDTO formProgresssDTO, ExportByFormListResponseDTO exportByFormListResponseDTO) {
        int i = 1;
        for (AnswerDTO answersByFormProgress : getAnswersByFormProgress(formProgresssDTO.getId())) {
            if (i % 9 == 1) {
                objectiveOfTheProjectSetter(exportByFormListResponseDTO, answersByFormProgress);
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
                geographicalFocusAriaSetter(exportByFormListResponseDTO, answersByFormProgress);
            }
            i++;
        }
    }

    private void objectiveOfTheProjectSetter(ExportByFormListResponseDTO exportByFormListResponseDTO, AnswerDTO answersByFormProgress) {
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
    }

    private void geographicalFocusAriaSetter(ExportByFormListResponseDTO exportByFormListResponseDTO, AnswerDTO answersByFormProgress) {
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
