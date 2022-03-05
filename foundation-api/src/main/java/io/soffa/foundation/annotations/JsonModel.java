package io.soffa.foundation.annotations;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.lang.annotation.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface JsonModel{}
