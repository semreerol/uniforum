package com.bunyaminkalkan.api.annotations;

import com.bunyaminkalkan.api.validators.UniqueUserNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUserNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserName {

    String message() default "This username is already in use.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}