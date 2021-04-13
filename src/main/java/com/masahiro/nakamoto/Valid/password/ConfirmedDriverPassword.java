package com.masahiro.nakamoto.Valid.password;

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
@Constraint(validatedBy = ConfirmedDriverPasswordValidator.class)
public @interface ConfirmedDriverPassword {

	String message() default "パスワードとパスワード再入力が一致しません";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
