package com.masahiro.nakamoto.domain.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.masahiro.nakamoto.Valid.group.ValidGroup1;
import com.masahiro.nakamoto.Valid.group.ValidGroup2;
import com.masahiro.nakamoto.Valid.group.ValidGroup3;

import lombok.Data;

/**
 * 現在のパスワードを入力するフォームオブジェクト
 */
@Data
public class PassChangeForm {

	/**
	 * パスワード
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Size(min = 8, max = 100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String password;

}
