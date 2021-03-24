package com.masahiro.nakamoto.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class RegisterHolidayForm {

	private String holiday;

	private List<LocalDate> holidayList;

}
