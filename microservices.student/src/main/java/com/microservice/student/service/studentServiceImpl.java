package com.microservice.student.service;

import com.microservice.student.entity.student;
import com.microservice.student.persistence.studentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class studentServiceImpl implements IstudetnService
{
@Autowired
    private studentRepository studentRepository;

    @Override
    public List<student> findAllStudents() {
        return (List<student>) studentRepository.findAll();
    }

    @Override
    public student findStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Override
    public void saveStudent(student student) {
        studentRepository.save(student);
    }

    @Override
    public List<student> findByCourseId(Long courseId) {
        return studentRepository.findByCourseId(courseId);
    }
}
