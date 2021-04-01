package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.Area;

@Mapper
public interface AreaMapper {

	/**
	 * 指定した拠点コードの拠点名を検索
	 *
	 * @param areaId
	 * @return
	 */
	public Area findAreaName(Integer areaId);

	public Integer findAreaId(String id);

}
