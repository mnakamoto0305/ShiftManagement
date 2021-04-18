package com.masahiro.nakamoto.Valid.id;

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
			context.buildConstraintViolationWithTemplate("\u30e1\u30fc\u30eb\u30a2\u30c9\u30ec\u30b9\u3068\u30e1\u30fc\u30eb\u30a2\u30c9\u30ec\u30b9\u518d\u5165\u529b\u304c\u4e00\u81f4\u3057\u307e\u305b\u3093")
				.addPropertyNode("idConfirm")
				.addConstraintViolation();
			return false;
		}
	}

}
