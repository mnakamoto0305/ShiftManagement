package com.masahiro.nakamoto;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.masahiro.nakamoto.domain.Driver;

public class ConfirmedDriverIdValidator implements ConstraintValidator<ConfirmedDriverId, Driver> {

	@Override
	public boolean isValid(Driver value, ConstraintValidatorContext context) {
		if (Objects.equals(value.getId(), value.getIdConfirm())) {
			return true;
		} else {
			context.buildConstraintViolationWithTemplate("メールアドレスとメールアドレス再入力が一致しません")
				.addPropertyNode("idConfirm")
				.addConstraintViolation();
			return false;
		}
	}

}
