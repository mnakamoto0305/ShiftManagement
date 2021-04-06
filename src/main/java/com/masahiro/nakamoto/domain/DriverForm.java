package com.masahiro.nakamoto.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DriverForm {

	private String searchWord;

	private int areaId;

}