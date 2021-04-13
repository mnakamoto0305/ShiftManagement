package com.masahiro.nakamoto.Valid.password;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.masahiro.nakamoto.domain.Employee;

public class ConfirmedPasswordValidator implements ConstraintValidator<ConfirmedPassword, Employee> {

	@Override
	public boolean isValid(Employee value, ConstraintValidatorContext context) {
		if (Objects.equals(value.getPassword(), value.getPasswordConfirm())) {
			return true;
		} else {
			context.buildConstraintViolationWithTemplate("パスワードとパスワード再入力が一致しません")
				.addPropertyNode("passwordConfirm")
				.addConstraintViolation();
			return false;
		}
	}

}
