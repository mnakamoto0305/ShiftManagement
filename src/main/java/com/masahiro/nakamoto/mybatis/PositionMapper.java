package com.masahiro.nakamoto.mybatis;

import org.apache.ibatis.annotations.Mapper;

/**
 * 社員とドライバーを判断するリポジトリ
 */
@Mapper
public interface PositionMapper {

	/**
	 * 指定したIDのポジションIDを返す
	 *
	 * @param id
	 * @return
	 */
	public int findPosition(String id);

}
