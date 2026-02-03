package com.microservice.course.client;

import com.microservice.course.controller.dto.studentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-student", url = "localhost:8080/api/student")
public interface studentClient {
    @GetMapping("/serach_by_id_course/{idCourse}")
List<studentDTO> findAllStudentByCourse(@PathVariable Long idCourse);
}
