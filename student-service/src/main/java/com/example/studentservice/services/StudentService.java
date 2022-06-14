package com.example.studentservice.services;

import com.example.studentservice.dtos.AddressDTO;
import com.example.studentservice.dtos.StudentDTO;
import com.example.studentservice.dtos.StudentMapper;
import com.example.studentservice.entities.Student;
import com.example.studentservice.feignclients.AddressFeignClient;
import com.example.studentservice.repositories.StudentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AddressService addressService;

    public StudentDTO getStudentById(int id){
        Student student =  studentRepository.findById(id).orElse(null);
        if(student != null){
            StudentDTO studentDTO = StudentMapper.INSTANCE.studentToStudentDTO(student);
            AddressDTO addressResponse = addressService.getAddressById(student.getAddressId());
            studentDTO.setAddress(addressResponse);
            return studentDTO;
        }
        return null;
    }

}
