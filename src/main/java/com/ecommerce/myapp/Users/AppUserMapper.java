package com.ecommerce.myapp.Users;

import com.ecommerce.myapp.Users.Entity.AppUser;
import com.ecommerce.myapp.Users.Dto.AppUserDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppUserMapper {

    AppUser toEntity(AppUserDto appUserDto2);

    AppUserDto toDto(AppUser appUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AppUser partialUpdate(AppUserDto appUserDto2, @MappingTarget AppUser appUser);
}