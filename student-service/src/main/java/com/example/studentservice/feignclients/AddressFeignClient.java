package com.example.studentservice.feignclients;

import com.example.studentservice.dtos.AddressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient( value = "api-gateway")
public interface AddressFeignClient {
    @GetMapping("/address-service/api/address/{id}")
    public AddressDTO getAddressById(@PathVariable(name = "id") int id);
}
