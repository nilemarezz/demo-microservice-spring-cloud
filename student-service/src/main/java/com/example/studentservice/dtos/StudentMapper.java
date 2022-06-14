package com.example.studentservice.dtos;

import com.example.studentservice.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "first_name" , source = "firstName")
    @Mapping(target = "last_name" , source = "lastName")
    StudentDTO studentToStudentDTO(Student student);
}
