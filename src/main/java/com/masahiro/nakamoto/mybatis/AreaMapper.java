package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.Area;

@Mapper
public interface AreaMapper {

	public Area findAreaName(int areaId);

}
