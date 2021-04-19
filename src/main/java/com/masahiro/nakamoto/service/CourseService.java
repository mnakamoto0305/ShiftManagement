package com.masahiro.nakamoto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.mybatis.CourseMapper;

/**
 * コース情報に関する処理を行うサービス
 */
@Service
public class CourseService {

	@Autowired
	CourseMapper courseMapper;

	/**
	 * 指定したドライバーが担当しているコース番号を返す
	 *
	 * @param id
	 * @return
	 */
	public int findCourseId(String id) {
		return courseMapper.findCourseId(id);
	}

}
