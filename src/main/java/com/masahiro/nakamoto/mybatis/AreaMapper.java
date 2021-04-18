package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.Area;

/**
 * 拠点情報に関するリポジトリ
 */
@Mapper
public interface AreaMapper {

	/**
	 * 指定した拠点コードの拠点名を検索
	 *
	 * @param areaId
	 * @return
	 */
	public Area findAreaName(Integer areaId);

	/**
	 * 指定した社員の担当拠点を取得
	 *
	 * @param id
	 * @return
	 */
	public Integer findAreaId(String id);

	/**
	 * 指定した拠点の総ドライバー数を取得
	 *
	 * @param areaId
	 * @return
	 */
	public int findTotalDrivers(int areaId);

	/**
	 * 指定した拠点の総コース数を取得
	 *
	 * @param areaId
	 * @return
	 */
	public int findTotalCourses(int areaId);

}
