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

	String message() default "\u30d1\u30b9\u30ef\u30fc\u30c9\u3068\u30d1\u30b9\u30ef\u30fc\u30c9\u518d\u5165\u529b\u304c\u4e00\u81f4\u3057\u307e\u305b\u3093";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
