package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

/**
 * コース情報に関するリポジトリ
 */
@Mapper
public interface CourseMapper {

	/**
	 * 指定したドライバの担当コース番号を取得
	 *
	 * @param id
	 * @return
	 */
	public int findCourseId(String id);

	/**
	 * 指定した拠点のコース数を取得する
	 *
	 * @param areaId
	 * @return
	 */
	public int findTotalCourses(int areaId);

}
