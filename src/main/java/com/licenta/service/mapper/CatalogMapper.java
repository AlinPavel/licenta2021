package com.licenta.service.mapper;

import com.licenta.domain.*;
import com.licenta.service.dto.CatalogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Catalog} and its DTO {@link CatalogDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface CatalogMapper extends EntityMapper<CatalogDTO, Catalog> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "idAppUser")
    CatalogDTO toDto(Catalog s);
}
