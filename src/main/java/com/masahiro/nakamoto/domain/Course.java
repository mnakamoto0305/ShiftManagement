package com.masahiro.nakamoto.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Course {

	private Integer totalCourses;

	private Integer courseId;

}
