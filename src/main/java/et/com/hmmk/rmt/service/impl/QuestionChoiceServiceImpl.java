package et.com.hmmk.rmt.service.impl;

import et.com.hmmk.rmt.domain.QuestionChoice;
import et.com.hmmk.rmt.repository.QuestionChoiceRepository;
import et.com.hmmk.rmt.service.QuestionChoiceService;
import et.com.hmmk.rmt.service.dto.QuestionChoiceDTO;
import et.com.hmmk.rmt.service.mapper.QuestionChoiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionChoice}.
 */
@Service
@Transactional
public class QuestionChoiceServiceImpl implements QuestionChoiceService {

    private final Logger log = LoggerFactory.getLogger(QuestionChoiceServiceImpl.class);

    private final QuestionChoiceRepository questionChoiceRepository;

    private final QuestionChoiceMapper questionChoiceMapper;

    public QuestionChoiceServiceImpl(QuestionChoiceRepository questionChoiceRepository, QuestionChoiceMapper questionChoiceMapper) {
        this.questionChoiceRepository = questionChoiceRepository;
        this.questionChoiceMapper = questionChoiceMapper;
    }

    @Override
    public QuestionChoiceDTO save(QuestionChoiceDTO questionChoiceDTO) {
        log.debug("Request to save QuestionChoice : {}", questionChoiceDTO);
        QuestionChoice questionChoice = questionChoiceMapper.toEntity(questionChoiceDTO);
        questionChoice = questionChoiceRepository.save(questionChoice);
        return questionChoiceMapper.toDto(questionChoice);
    }

    @Override
    public Optional<QuestionChoiceDTO> partialUpdate(QuestionChoiceDTO questionChoiceDTO) {
        log.debug("Request to partially update QuestionChoice : {}", questionChoiceDTO);

        return questionChoiceRepository
            .findById(questionChoiceDTO.getId())
            .map(existingQuestionChoice -> {
                questionChoiceMapper.partialUpdate(existingQuestionChoice, questionChoiceDTO);

                return existingQuestionChoice;
            })
            .map(questionChoiceRepository::save)
            .map(questionChoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionChoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionChoices");
        return questionChoiceRepository.findAll(pageable).map(questionChoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionChoiceDTO> findOne(Long id) {
        log.debug("Request to get QuestionChoice : {}", id);
        return questionChoiceRepository.findById(id).map(questionChoiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionChoice : {}", id);
        questionChoiceRepository.deleteById(id);
    }
}
