package com.masahiro.nakamoto.domain;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterHolidayForm {

	@Size(min=87,max=87,message="休み希望は8日分入力してください")
	private String holiday;

	private List<LocalDate> holidayList;

}
