package com.masahiro.nakamoto.domain.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.Valid.group.ValidGroup1;
import com.masahiro.nakamoto.Valid.group.ValidGroup2;
import com.masahiro.nakamoto.Valid.id.ConfirmedUpdateId;

import lombok.Data;

@Data
@Component
@ConfirmedUpdateId(groups = ValidGroup2.class)
public class InfomationForm {

	/**
	 * 社員ID
	 */
	@NotBlank(groups = ValidGroup1.class)
	private String id;

	/**
	 * 社員ID再入力
	 */
	@NotBlank(groups = ValidGroup1.class)
	private String idConfirm;

	private String previousId;

	/**
	 * 電話番号
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Pattern(regexp="^0\\d{9,10}$", groups = ValidGroup2.class)
	private String phoneNumber;

	/**
	 * 郵便番号
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Pattern(regexp="^[0-9]{7}$", groups = ValidGroup2.class)
	private String postalCode;

	/**
	 * 住所
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Size(max = 100, groups = ValidGroup2.class)
	private String address;

	public InfomationForm(@NotBlank(groups = ValidGroup1.class) String id,
			@NotBlank(groups = ValidGroup1.class) @Pattern(regexp = "^0\\d{9,10}$", groups = ValidGroup2.class) String phoneNumber,
			@NotBlank(groups = ValidGroup1.class) @Pattern(regexp = "^[0-9]{7}$", groups = ValidGroup2.class) String postalCode,
			@NotBlank(groups = ValidGroup1.class) @Size(max = 100, groups = ValidGroup2.class) String address) {
		super();
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.postalCode = postalCode;
		this.address = address;
	}

	/**
	 * 引数なしコンストラクタ
	 */
	public InfomationForm() {
		super();
	}



}
