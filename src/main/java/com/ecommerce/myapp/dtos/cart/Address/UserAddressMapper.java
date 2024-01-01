package com.ecommerce.myapp.dtos.cart.Address;

import com.ecommerce.myapp.model.client.UserAddress;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAddressMapper {
    UserAddress toEntity(UserAddressDto userAddressDto);

    UserAddressDto toDto(UserAddress userAddress);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserAddress partialUpdate(UserAddressDto userAddressDto, @MappingTarget UserAddress userAddress);
}