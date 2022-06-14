package com.example.addressservice.services;

import com.example.addressservice.entities.Address;
import com.example.addressservice.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address add(Address address){
        return addressRepository.save(address);
    }

    public Address getAddressById(int id){
        return addressRepository.findById(id).orElse( null);
    }
}
