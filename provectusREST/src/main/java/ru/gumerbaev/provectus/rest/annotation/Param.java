package ru.gumerbaev.provectus.rest.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

	String value();
}
