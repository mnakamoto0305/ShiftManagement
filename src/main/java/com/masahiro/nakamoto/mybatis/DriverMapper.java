package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.Driver;

@Mapper
public interface DriverMapper {

	public Driver findDriverInfo(String id);

}
