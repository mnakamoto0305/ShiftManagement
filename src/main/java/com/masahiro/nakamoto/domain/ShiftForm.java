package com.masahiro.nakamoto.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShiftForm {

	//private String designatedDate;
	private Integer area;
	private LocalDate date;
	private String year;
	private String month;

}
