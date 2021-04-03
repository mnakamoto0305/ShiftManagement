package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PositionMapper {

	public int findPosition(String id);

}
