package com.masahiro.nakamoto.domain.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.masahiro.nakamoto.Valid.group.ValidGroup1;
import com.masahiro.nakamoto.Valid.group.ValidGroup2;
import com.masahiro.nakamoto.Valid.group.ValidGroup3;
import com.masahiro.nakamoto.Valid.password.ConfirmedChangePassword;

import lombok.Data;

/**
 * 新規のパスワードを登録するフォームオブジェクト
 */
@Data
@ConfirmedChangePassword(groups = ValidGroup2.class)
public class PassChangeConfirmForm {

	/**
	 * パスワード
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Size(min = 8, max = 100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String password;

	/**
	 * パスワード(確認用)
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Size(min = 8, max = 100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String passwordConfirm;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 引数なしコンストラクタ
	 */
	public PassChangeConfirmForm() {
		super();
	}

	/**
	 * コンストラクタ
	 *
	 * @param password
	 * @param passwordConfirm
	 */
	public PassChangeConfirmForm(
			@NotBlank(groups = ValidGroup1.class) @Size(min = 8, max = 100, groups = ValidGroup2.class) @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class) String password,
			@NotBlank(groups = ValidGroup1.class) @Size(min = 8, max = 100, groups = ValidGroup2.class) @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class) String passwordConfirm) {
		super();
		this.password = password;
		this.passwordConfirm = passwordConfirm;
	}


}
