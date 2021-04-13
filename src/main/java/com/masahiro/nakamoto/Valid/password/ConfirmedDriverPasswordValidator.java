package com.masahiro.nakamoto.Valid.password;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.masahiro.nakamoto.domain.Driver;

public class ConfirmedDriverPasswordValidator implements ConstraintValidator<ConfirmedDriverPassword, Driver> {

	@Override
	public boolean isValid(Driver value, ConstraintValidatorContext context) {
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
