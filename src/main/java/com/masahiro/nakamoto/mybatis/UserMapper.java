package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.User;

@Mapper
public interface UserMapper {

	public User identifyUser(String id);
}