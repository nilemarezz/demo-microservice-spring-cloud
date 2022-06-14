package com.example.addressservice.dtos;

import com.example.addressservice.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "address_id" , source = "id")
    AddressDTO addressToAddressDTO(Address address);
}
