package com.licenta.service.impl;

import com.licenta.domain.Sport;
import com.licenta.repository.SportRepository;
import com.licenta.service.SportService;
import com.licenta.service.dto.SportDTO;
import com.licenta.service.mapper.SportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sport}.
 */
@Service
@Transactional
public class SportServiceImpl implements SportService {

    private final Logger log = LoggerFactory.getLogger(SportServiceImpl.class);

    private final SportRepository sportRepository;

    private final SportMapper sportMapper;

    public SportServiceImpl(SportRepository sportRepository, SportMapper sportMapper) {
        this.sportRepository = sportRepository;
        this.sportMapper = sportMapper;
    }

    @Override
    public SportDTO save(SportDTO sportDTO) {
        log.debug("Request to save Sport : {}", sportDTO);
        Sport sport = sportMapper.toEntity(sportDTO);
        sport = sportRepository.save(sport);
        return sportMapper.toDto(sport);
    }

    @Override
    public Optional<SportDTO> partialUpdate(SportDTO sportDTO) {
        log.debug("Request to partially update Sport : {}", sportDTO);

        return sportRepository
            .findById(sportDTO.getIdSport())
            .map(
                existingSport -> {
                    sportMapper.partialUpdate(existingSport, sportDTO);
                    return existingSport;
                }
            )
            .map(sportRepository::save)
            .map(sportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sports");
        return sportRepository.findAll(pageable).map(sportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SportDTO> findOne(Long id) {
        log.debug("Request to get Sport : {}", id);
        return sportRepository.findById(id).map(sportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sport : {}", id);
        sportRepository.deleteById(id);
    }
}
