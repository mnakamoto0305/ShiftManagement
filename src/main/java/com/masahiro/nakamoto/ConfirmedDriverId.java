package com.masahiro.nakamoto;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = ConfirmedDriverIdValidator.class)
public @interface ConfirmedDriverId {

	String message() default "メールアドレスとメールアドレス再入力が一致しません";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
