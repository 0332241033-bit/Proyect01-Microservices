package com.microservice.student.service;

import com.microservice.student.entity.student;

import java.util.List;

public interface IstudetnService {

    List<student> findAllStudents();
    student findStudentById(Long id);
    void saveStudent(student student);
   // void deleteStudent(Long id);
    List<student> findByCourseId(Long courseId);

}
