package com.masahiro.nakamoto.Valid.id;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.masahiro.nakamoto.domain.form.InfomationForm;

public class ConfirmedUpdateIdValidator implements ConstraintValidator<ConfirmedUpdateId, InfomationForm> {

	@Override
	public boolean isValid(InfomationForm value, ConstraintValidatorContext context) {
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
