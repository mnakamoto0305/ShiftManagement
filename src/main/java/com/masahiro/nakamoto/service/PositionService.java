package com.masahiro.nakamoto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.mybatis.PositionMapper;

@Service
public class PositionService {

	@Autowired
	PositionMapper positionMapper;


	public int findPosition(String id) {
		return positionMapper.findPosition(id);
	}

}
