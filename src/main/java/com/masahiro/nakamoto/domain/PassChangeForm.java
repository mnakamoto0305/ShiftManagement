package com.masahiro.nakamoto.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.masahiro.nakamoto.Valid.ValidGroup1;
import com.masahiro.nakamoto.Valid.ValidGroup2;
import com.masahiro.nakamoto.Valid.ValidGroup3;

import lombok.Data;

@Data
public class PassChangeForm {

	@NotBlank(groups = ValidGroup1.class)
	@Length(min=8,max=100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String password;

}
