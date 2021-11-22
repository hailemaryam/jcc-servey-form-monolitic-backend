package et.com.hmmk.rmt.service.impl;

import et.com.hmmk.rmt.domain.FormProgresss;
import et.com.hmmk.rmt.repository.FormProgresssRepository;
import et.com.hmmk.rmt.service.FormProgresssService;
import et.com.hmmk.rmt.service.dto.FormProgresssDTO;
import et.com.hmmk.rmt.service.mapper.FormProgresssMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FormProgresss}.
 */
@Service
@Transactional
public class FormProgresssServiceImpl implements FormProgresssService {

    private final Logger log = LoggerFactory.getLogger(FormProgresssServiceImpl.class);

    private final FormProgresssRepository formProgresssRepository;

    private final FormProgresssMapper formProgresssMapper;

    public FormProgresssServiceImpl(FormProgresssRepository formProgresssRepository, FormProgresssMapper formProgresssMapper) {
        this.formProgresssRepository = formProgresssRepository;
        this.formProgresssMapper = formProgresssMapper;
    }

    @Override
    public FormProgresssDTO save(FormProgresssDTO formProgresssDTO) {
        log.debug("Request to save FormProgresss : {}", formProgresssDTO);
        FormProgresss formProgresss = formProgresssMapper.toEntity(formProgresssDTO);
        formProgresss = formProgresssRepository.save(formProgresss);
        return formProgresssMapper.toDto(formProgresss);
    }

    @Override
    public Optional<FormProgresssDTO> partialUpdate(FormProgresssDTO formProgresssDTO) {
        log.debug("Request to partially update FormProgresss : {}", formProgresssDTO);

        return formProgresssRepository
            .findById(formProgresssDTO.getId())
            .map(existingFormProgresss -> {
                formProgresssMapper.partialUpdate(existingFormProgresss, formProgresssDTO);

                return existingFormProgresss;
            })
            .map(formProgresssRepository::save)
            .map(formProgresssMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormProgresssDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormProgressses");
        return formProgresssRepository.findAll(pageable).map(formProgresssMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormProgresssDTO> findOne(Long id) {
        log.debug("Request to get FormProgresss : {}", id);
        return formProgresssRepository.findById(id).map(formProgresssMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormProgresss : {}", id);
        formProgresssRepository.deleteById(id);
    }
}
