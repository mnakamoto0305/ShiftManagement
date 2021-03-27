package com.masahiro.nakamoto.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ShiftForm {

	private String designatedDate;
	private LocalDate first;
	private LocalDate last;
	private Integer area;

}
