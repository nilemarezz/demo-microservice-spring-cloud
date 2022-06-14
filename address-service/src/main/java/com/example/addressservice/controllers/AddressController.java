package com.example.addressservice.controllers;

import com.example.addressservice.dtos.AddressDTO;
import com.example.addressservice.dtos.AddressMapper;
import com.example.addressservice.entities.Address;
import com.example.addressservice.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public AddressDTO add(@RequestBody Address address){
        Address res =  addressService.add(address);
        AddressDTO addressDTO = AddressMapper.INSTANCE.addressToAddressDTO(res);
        return addressDTO;
    }
    @GetMapping("/{id}")
    public AddressDTO getById(@PathVariable(name = "id") int id){
        Address address =  addressService.getAddressById(id);
        AddressDTO addressDTO = AddressMapper.INSTANCE.addressToAddressDTO(address);
        return addressDTO;
    }
}
