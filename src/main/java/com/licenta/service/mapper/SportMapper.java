package com.licenta.service.mapper;

import com.licenta.domain.*;
import com.licenta.service.dto.SportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sport} and its DTO {@link SportDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface SportMapper extends EntityMapper<SportDTO, Sport> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "idAppUser")
    SportDTO toDto(Sport s);
}
