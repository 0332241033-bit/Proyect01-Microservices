package com.microservice.course.controller;

import com.microservice.course.entity.course;
import com.microservice.course.service.IcourseService;
import com.microservice.course.service.courseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class courseController {
    @Autowired
    private IcourseService courseService;



        @PostMapping("/create")
        @ResponseStatus(HttpStatus.CREATED)
        public void saveStudent(@RequestBody course student) {
         courseService.save(student);
        }

        @GetMapping("/all")
        public ResponseEntity<List<course>> findAllStudent() {
            List<course> students = courseService.findAll();
            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(students);
        }



        @GetMapping("/search/{id}")
        public ResponseEntity<?> fynByid(@PathVariable Long id) {
            course course = courseService.findById(id);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(course);
        }
        @GetMapping("/search_students/{idCourse}")
        public ResponseEntity<?> findStudentByCourse(@PathVariable Long idCourse) {
            return ResponseEntity.ok(courseService.findStudentByCourse(idCourse));
        }


}
