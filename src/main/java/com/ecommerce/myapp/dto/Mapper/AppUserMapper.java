package com.ecommerce.myapp.dto.Mapper;

import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.dto.admin.AppUserDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppUserMapper {

    AppUser toEntity(AppUserDto appUserDto2);

    AppUserDto toDto(AppUser appUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AppUser partialUpdate(AppUserDto appUserDto2, @MappingTarget AppUser appUser);
}