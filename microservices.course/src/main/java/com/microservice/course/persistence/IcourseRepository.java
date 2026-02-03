package com.microservice.course.persistence;

import com.microservice.course.entity.course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcourseRepository extends CrudRepository<course,Long> {
}
