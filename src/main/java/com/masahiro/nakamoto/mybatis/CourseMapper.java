 package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {

	/**
	 * 指定したドライバの担当コース番号を取得
	 *
	 * @param id
	 * @return
	 */
	public int findCourseId(String id);

}
