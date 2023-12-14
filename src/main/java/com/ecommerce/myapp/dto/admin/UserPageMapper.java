package com.ecommerce.myapp.dto.admin;

import com.ecommerce.myapp.model.user.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPageMapper {
    ResListUsers toDto(AppUser appUser);
}