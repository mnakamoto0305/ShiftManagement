package com.masahiro.nakamoto.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masahiro.nakamoto.domain.Area;
import com.masahiro.nakamoto.mybatis.AreaMapper;

@Service
public class AreaService {

	@Autowired
	AreaMapper areaMapper;

	@Transactional
	public Area findAreaName(Integer areaId) {
		return areaMapper.findAreaName(areaId);
	}

	@Transactional
	public Integer findAreaId(String id) {
		return areaMapper.findAreaId(id);
	}

}
