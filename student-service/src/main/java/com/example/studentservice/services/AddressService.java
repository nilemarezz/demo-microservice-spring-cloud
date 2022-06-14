package com.example.studentservice.services;

import com.example.studentservice.dtos.AddressDTO;
import com.example.studentservice.feignclients.AddressFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    Logger logger = LoggerFactory.getLogger(AddressService.class);
    int i = 1;

    @Autowired
    private AddressFeignClient addressFeignClient;

    @CircuitBreaker(name = "addressService",fallbackMethod = "fallbackGetAddressById")
    public AddressDTO getAddressById(int id){
        logger.info("Count : " + i);
        i++;
        return addressFeignClient.getAddressById(id);
    }

    public AddressDTO fallbackGetAddressById(int id , Throwable th){
        logger.error("Error : " + th);
        return new AddressDTO();
    }
}
