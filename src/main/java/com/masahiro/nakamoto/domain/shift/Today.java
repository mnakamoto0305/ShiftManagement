package com.masahiro.nakamoto.domain.shift;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Today {

	private Boolean isAttendance;
	private int areaId;
	private int courseId;
	private String areaName;

}
