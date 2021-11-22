package et.com.hmmk.rmt.service.impl;

import et.com.hmmk.rmt.domain.MultipleChoiceAnsewer;
import et.com.hmmk.rmt.repository.MultipleChoiceAnsewerRepository;
import et.com.hmmk.rmt.service.MultipleChoiceAnsewerService;
import et.com.hmmk.rmt.service.dto.MultipleChoiceAnsewerDTO;
import et.com.hmmk.rmt.service.mapper.MultipleChoiceAnsewerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MultipleChoiceAnsewer}.
 */
@Service
@Transactional
public class MultipleChoiceAnsewerServiceImpl implements MultipleChoiceAnsewerService {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceAnsewerServiceImpl.class);

    private final MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository;

    private final MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper;

    public MultipleChoiceAnsewerServiceImpl(
        MultipleChoiceAnsewerRepository multipleChoiceAnsewerRepository,
        MultipleChoiceAnsewerMapper multipleChoiceAnsewerMapper
    ) {
        this.multipleChoiceAnsewerRepository = multipleChoiceAnsewerRepository;
        this.multipleChoiceAnsewerMapper = multipleChoiceAnsewerMapper;
    }

    @Override
    public MultipleChoiceAnsewerDTO save(MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO) {
        log.debug("Request to save MultipleChoiceAnsewer : {}", multipleChoiceAnsewerDTO);
        MultipleChoiceAnsewer multipleChoiceAnsewer = multipleChoiceAnsewerMapper.toEntity(multipleChoiceAnsewerDTO);
        multipleChoiceAnsewer = multipleChoiceAnsewerRepository.save(multipleChoiceAnsewer);
        return multipleChoiceAnsewerMapper.toDto(multipleChoiceAnsewer);
    }

    @Override
    public Optional<MultipleChoiceAnsewerDTO> partialUpdate(MultipleChoiceAnsewerDTO multipleChoiceAnsewerDTO) {
        log.debug("Request to partially update MultipleChoiceAnsewer : {}", multipleChoiceAnsewerDTO);

        return multipleChoiceAnsewerRepository
            .findById(multipleChoiceAnsewerDTO.getId())
            .map(existingMultipleChoiceAnsewer -> {
                multipleChoiceAnsewerMapper.partialUpdate(existingMultipleChoiceAnsewer, multipleChoiceAnsewerDTO);

                return existingMultipleChoiceAnsewer;
            })
            .map(multipleChoiceAnsewerRepository::save)
            .map(multipleChoiceAnsewerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MultipleChoiceAnsewerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MultipleChoiceAnsewers");
        return multipleChoiceAnsewerRepository.findAll(pageable).map(multipleChoiceAnsewerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MultipleChoiceAnsewerDTO> findOne(Long id) {
        log.debug("Request to get MultipleChoiceAnsewer : {}", id);
        return multipleChoiceAnsewerRepository.findById(id).map(multipleChoiceAnsewerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MultipleChoiceAnsewer : {}", id);
        multipleChoiceAnsewerRepository.deleteById(id);
    }
}
