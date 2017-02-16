package ru.gumerbaev.provectus.rest.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Rest {

	enum RestType {
		GET, POST, DELETE;
	}

	RestType type();

	String path() default "/";
}
