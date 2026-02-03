package com.microservice.student.persistence;

import com.microservice.student.entity.student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface studentRepository extends CrudRepository<student,Long> {
    @Query("SELECT s FROM student s WHERE s.courseId = :courseId")
    List<student> findByCourseId(Long courseId);
    //List<student> findByCourseId(Long courseId);
}
