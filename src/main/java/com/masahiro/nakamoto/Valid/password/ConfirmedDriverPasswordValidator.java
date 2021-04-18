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
			context.buildConstraintViolationWithTemplate("\u30d1\u30b9\u30ef\u30fc\u30c9\u3068\u30d1\u30b9\u30ef\u30fc\u30c9\u518d\u5165\u529b\u304c\u4e00\u81f4\u3057\u307e\u305b\u3093")
				.addPropertyNode("passwordConfirm")
				.addConstraintViolation();
			return false;
		}
	}

}
