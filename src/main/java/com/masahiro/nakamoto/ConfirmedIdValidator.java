package com.masahiro.nakamoto;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.masahiro.nakamoto.domain.Employee;

public class ConfirmedIdValidator implements ConstraintValidator<ConfirmedId, Employee> {

	@Override
	public boolean isValid(Employee value, ConstraintValidatorContext context) {
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
