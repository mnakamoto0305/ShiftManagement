package com.masahiro.nakamoto.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * コース情報を表現するオブジェクト
 */
@Data
@Component
public class Course {

	/**
	 * コースの総数
	 */
	private Integer totalCourses;

	/**
	 * ドライバーの総数
	 */
	private Integer totalDrivers;

	/**
	 * コースID
	 */
	private Integer courseId;

}
