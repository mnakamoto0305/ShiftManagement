package com.masahiro.nakamoto.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.domain.Area;
import com.masahiro.nakamoto.mybatis.AreaMapper;

@Service
public class AreaService {

	@Autowired
	AreaMapper areaMapper;

	public Area findAreaName(int areaId) {
		return areaMapper.findAreaName(areaId);
	}

}
