package com.microservice.course.controller.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class studentDTO {
    private String name ;

    private String lastname ;
    private String email ;

    private Long courseId ;
}
