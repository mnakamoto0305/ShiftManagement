package com.masahiro.nakamoto.Valid.id;

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
@Constraint(validatedBy = ConfirmedUpdateIdValidator.class)
public @interface ConfirmedUpdateId {

	String message() default "\u30e1\u30fc\u30eb\u30a2\u30c9\u30ec\u30b9\u3068\u30e1\u30fc\u30eb\u30a2\u30c9\u30ec\u30b9\u518d\u5165\u529b\u304c\u4e00\u81f4\u3057\u307e\u305b\u3093";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
