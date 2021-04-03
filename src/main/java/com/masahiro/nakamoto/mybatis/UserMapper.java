package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.masahiro.nakamoto.domain.SiteUser;

@Mapper
public interface UserMapper {

	/**
	 * 入力されたIDからUser情報を取得
	 *
	 * @param id
	 * @return
	 */
	public SiteUser identifyUser(String id);
}