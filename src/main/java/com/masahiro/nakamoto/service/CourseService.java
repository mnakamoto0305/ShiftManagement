package com.masahiro.nakamoto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.mybatis.CourseMapper;

@Service
public class CourseService {

	@Autowired
	CourseMapper courseMapper;

	@Transactional
	public int findCourseId(String id) {
		return courseMapper.findCourseId(id);
	}

}
