package com.example.studentservice.controllers;

import com.example.studentservice.dtos.StudentDTO;
import com.example.studentservice.feignclients.AddressFeignClient;
import com.example.studentservice.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private AddressFeignClient addressFeignClient;

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public StudentDTO getStudent(@PathVariable(name = "id") int id ){
        StudentDTO student = studentService.getStudentById(id);
        return student;
    }
}
