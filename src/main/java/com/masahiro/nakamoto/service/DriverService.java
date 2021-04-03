package com.masahiro.nakamoto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masahiro.nakamoto.domain.Driver;
import com.masahiro.nakamoto.mybatis.DriverMapper;

@Service
public class DriverService {

	@Autowired
	DriverMapper driverMapper;

	public Driver findDriverInfo(String id) {
		return driverMapper.findDriverInfo(id);
	}

}
