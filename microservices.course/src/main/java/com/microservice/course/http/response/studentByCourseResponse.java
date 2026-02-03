package com.microservice.course.http.response;

import com.microservice.course.controller.dto.studentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class studentByCourseResponse {
    private String courseName;
    private String teacher ;
    private List<studentDTO> studentDTOList;

}
