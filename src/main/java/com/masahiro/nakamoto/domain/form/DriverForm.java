package com.masahiro.nakamoto.domain.form;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DriverForm {

	private String searchWord;

	private int areaId;

}
