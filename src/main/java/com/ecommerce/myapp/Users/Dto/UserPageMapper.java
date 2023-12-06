package com.ecommerce.myapp.Users.Dto;

import com.ecommerce.myapp.Users.Entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPageMapper {
    ResListUsers toDto(AppUser appUser);
}