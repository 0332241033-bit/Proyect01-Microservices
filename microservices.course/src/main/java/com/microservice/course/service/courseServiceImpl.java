package com.microservice.course.service;

import com.microservice.course.client.studentClient;
import com.microservice.course.controller.dto.studentDTO;
import com.microservice.course.entity.course;
import com.microservice.course.http.response.studentByCourseResponse;
import com.microservice.course.persistence.IcourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class courseServiceImpl implements  IcourseService {
   @Autowired
    private IcourseRepository IcourseRepository;
@Autowired
private studentClient studentClient;

    @Override
    public List<course> findAll() {
      return (List<course>) IcourseRepository.findAll();
    }

    @Override
    public course findById(Long id) {
        return IcourseRepository.findById(id).orElseThrow();
    }

    @Override
    public void save(course course) {
     IcourseRepository.save(course);
    }

    @Override
    public studentByCourseResponse findStudentByCourse(Long idCourse) {
        //Consultar el curso por id
        course course = IcourseRepository.findById(idCourse).orElseThrow();
        //Obtener la lista de estudiantes del curso
        List<studentDTO> studentDTOList = studentClient.findAllStudentByCourse(idCourse);


        return studentByCourseResponse.builder()
                .courseName(course.getName())
                .teacher(course.getTeacher())
                .studentDTOList(studentDTOList)
                .build();
    }
}
