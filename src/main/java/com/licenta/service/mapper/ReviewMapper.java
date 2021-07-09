package com.licenta.service.mapper;

import com.licenta.domain.*;
import com.licenta.service.dto.ReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class })
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "idAppUser")
    ReviewDTO toDto(Review s);
}
