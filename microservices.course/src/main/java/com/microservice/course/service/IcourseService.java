package com.microservice.course.service;

import com.microservice.course.entity.course;
import com.microservice.course.http.response.studentByCourseResponse;

import java.util.List;

public interface IcourseService {
    List<course> findAll();
    course findById(Long id);
    void save(course course);
    studentByCourseResponse findStudentByCourse(Long idCourse);


}
