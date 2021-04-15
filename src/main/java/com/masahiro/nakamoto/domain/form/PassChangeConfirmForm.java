package com.masahiro.nakamoto.domain.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.masahiro.nakamoto.Valid.group.ValidGroup1;
import com.masahiro.nakamoto.Valid.group.ValidGroup2;
import com.masahiro.nakamoto.Valid.group.ValidGroup3;
import com.masahiro.nakamoto.Valid.password.ConfirmedChangePassword;

import lombok.Data;

@Data
@ConfirmedChangePassword(groups = ValidGroup2.class)
public class PassChangeConfirmForm {

	@NotBlank(groups = ValidGroup1.class)
	@Length(min=8,max=100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String password;

	@NotBlank(groups = ValidGroup1.class)
	@Length(min=8,max=100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String passwordConfirm;

	private String id;

	public PassChangeConfirmForm(
			@NotBlank(groups = ValidGroup1.class) @Length(min = 8, max = 100, groups = ValidGroup2.class) @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class) String password,
			@NotBlank(groups = ValidGroup1.class) @Length(min = 8, max = 100, groups = ValidGroup2.class) @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class) String passwordConfirm) {
		super();
		this.password = password;
		this.passwordConfirm = passwordConfirm;
	}

	/**
	 * 引数なしコンストラクタ
	 */
	public PassChangeConfirmForm() {
		super();
	}


}
