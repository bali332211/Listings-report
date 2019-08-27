package com.worldofbooks.listingsreport.database.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
    @Constraint(validatedBy = UUIDValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UUIDConstraint {
        String message() default "Not UUID";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}


