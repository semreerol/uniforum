package com.bunyaminkalkan.api.annotations;

import com.bunyaminkalkan.api.validators.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Please enter a password that meets the following criteria:\n" +
            "- Length: 8 to 30 characters\n" +
            "- At least 1 uppercase letter\n" +
            "- At least 1 lowercase letter\n" +
            "- At least 1 digit\n" +
            "- At least 1 special character\n" +
            "- No sequences of 5 or more alphabetical, numerical, or QWERTY characters\n" +
            "- No repeating characters in a sequence of 3 or more characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
