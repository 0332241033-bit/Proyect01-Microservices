package com.microservice.student.controller;

import com.microservice.student.entity.student;
import com.microservice.student.service.studentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class studentController {
    @Autowired
    private studentServiceImpl studentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudent(@RequestBody student student) {
        studentService.saveStudent(student);
    }

    @GetMapping("/all")
    public ResponseEntity<List<student>> findAllStudent() {
        List<student> students = studentService.findAllStudents();
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }



@GetMapping("/search/{id}")
public ResponseEntity<?> fynByid(@PathVariable Long id) {
    student student = studentService.findStudentById(id);
    if (student == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(student);
}

@GetMapping("/serach_by_id_course/{idCourse}")
public ResponseEntity<?> findByIdCourse(@PathVariable Long idCourse) {
        return ResponseEntity.ok(studentService.findByCourseId(idCourse));
}




}
