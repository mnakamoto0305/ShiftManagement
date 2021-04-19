package com.masahiro.nakamoto.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.domain.Area;
import com.masahiro.nakamoto.mybatis.AreaMapper;

/**
 * 拠点情報に関する処理を行うサービス
 */
@Service
public class AreaService {

	@Autowired
	AreaMapper areaMapper;

	/**
	 * 指定した拠点IDの拠点名を取得
	 *
	 * @param areaId
	 * @return
	 */
	public Area findAreaName(Integer areaId) {
		return areaMapper.findAreaName(areaId);
	}

	/**
	 * 指定したドライバーの担当拠点を取得
	 *
	 * @param id
	 * @return
	 */
	public Integer findAreaId(String id) {
		return areaMapper.findAreaId(id);
	}

	/**
	 * 指定した拠点の総ドライバー数を取得
	 *
	 * @param areaId
	 * @return
	 */
	public int findTotalDrivers(int areaId) {
		return areaMapper.findTotalDrivers(areaId);
	}

	/**
	 * 指定した拠点の総コース数を取得
	 *
	 * @param areaId
	 * @return
	 */
	public int findTotalCourses(int areaId) {
		return areaMapper.findTotalCourses(areaId);
	}

}
