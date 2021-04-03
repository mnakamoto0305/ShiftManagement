package com.masahiro.nakamoto.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Substitute {


	private List<Integer> driverList;

}
