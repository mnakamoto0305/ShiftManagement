package com.masahiro.nakamoto.domain;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.ConfirmedDriverId;
import com.masahiro.nakamoto.ConfirmedDriverPassword;
import com.masahiro.nakamoto.Valid.ValidGroup1;
import com.masahiro.nakamoto.Valid.ValidGroup2;
import com.masahiro.nakamoto.Valid.ValidGroup3;

import lombok.Data;

@Data
@Component
@ConfirmedDriverPassword(groups = ValidGroup2.class)
@ConfirmedDriverId(groups = ValidGroup2.class)
public class Driver {

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
	 * 名字
	 */
	@NotBlank(groups = ValidGroup1.class)
	private String lastName;

	/**
	 * 名前
	 */
	@NotBlank(groups = ValidGroup1.class)
	private String firstName;

	/**
	 * 役職コード
	 */
	private Integer positionId;

	/**
	 * 性別
	 */
	@NotBlank(groups = ValidGroup1.class)
	private String sex;

	/**
	 * 生年月日
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Pattern(regexp="^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", groups = ValidGroup2.class)
	private String dateOfBirth;

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

	/**
	 * 入社日
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Pattern(regexp="^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", groups = ValidGroup2.class)
	private String joinDate;

	/**
	 * パスワード
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Length(min=8,max=100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String password;

	/**
	 * パスワード再入力
	 */
	@NotBlank(groups = ValidGroup1.class)
	@Length(min=8,max=100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class)
	private String passwordConfirm;

	/**
	 * パスワード更新日付
	 */
	private Date passUpdateDate;

	/**
	 * 権限
	 */
	@NotBlank(groups = ValidGroup1.class)
	private String role;

	/**
	 * 拠点ID
	 */
	@NotNull(groups = ValidGroup1.class)
	private Integer areaId;

	/**
	 * コース番号
	 */
	@NotNull(groups = ValidGroup1.class)
	private Integer courseId;

	/**
	 * 単価
	 */
	@NotNull(groups = ValidGroup1.class)
	@Min(value=0, groups = ValidGroup2.class)
	private Integer dailyWages;

	/**
	 * 経費
	 */
	@NotNull(groups = ValidGroup1.class)
	@Min(value=0, groups = ValidGroup2.class)
	private Integer monthlyExpenses;

}
