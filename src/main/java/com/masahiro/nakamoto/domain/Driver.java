package com.masahiro.nakamoto.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import com.masahiro.nakamoto.Valid.group.ValidGroup1;
import com.masahiro.nakamoto.Valid.group.ValidGroup2;
import com.masahiro.nakamoto.Valid.group.ValidGroup3;
import com.masahiro.nakamoto.Valid.id.ConfirmedDriverId;
import com.masahiro.nakamoto.Valid.password.ConfirmedDriverPassword;

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
	private Integer postalCode;

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
	private String passUpdateDate;

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

	private String payment;


	/**
	 *  引数なしコンストラクタ
	 */
	public Driver() {
		super();
	}

	public Driver(@NotBlank(groups = ValidGroup1.class) String id,
			@NotBlank(groups = ValidGroup1.class) String lastName,
			@NotBlank(groups = ValidGroup1.class) String firstName, Integer positionId,
			@NotBlank(groups = ValidGroup1.class) String sex,
			@NotBlank(groups = ValidGroup1.class) @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", groups = ValidGroup2.class) String dateOfBirth,
			@NotBlank(groups = ValidGroup1.class) @Pattern(regexp = "^0\\d{9,10}$", groups = ValidGroup2.class) String phoneNumber,
			@NotBlank(groups = ValidGroup1.class) @Pattern(regexp = "^[0-9]{7}$", groups = ValidGroup2.class) Integer postalCode,
			@NotBlank(groups = ValidGroup1.class) @Size(max = 100, groups = ValidGroup2.class) String address,
			@NotBlank(groups = ValidGroup1.class) @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", groups = ValidGroup2.class) String joinDate,
			@NotBlank(groups = ValidGroup1.class) @Length(min = 8, max = 100, groups = ValidGroup2.class) @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup3.class) String password,
			String passUpdateDate, @NotBlank(groups = ValidGroup1.class) String role,
			@NotNull(groups = ValidGroup1.class) Integer areaId, @NotNull(groups = ValidGroup1.class) Integer courseId,
			@NotNull(groups = ValidGroup1.class) @Min(value = 0, groups = ValidGroup2.class) Integer dailyWages,
			@NotNull(groups = ValidGroup1.class) @Min(value = 0, groups = ValidGroup2.class) Integer monthlyExpenses) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.positionId = positionId;
		this.sex = sex;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.postalCode = postalCode;
		this.address = address;
		this.joinDate = joinDate;
		this.password = password;
		this.passUpdateDate = passUpdateDate;
		this.role = role;
		this.areaId = areaId;
		this.courseId = courseId;
		this.dailyWages = dailyWages;
		this.monthlyExpenses = monthlyExpenses;
	}




}
