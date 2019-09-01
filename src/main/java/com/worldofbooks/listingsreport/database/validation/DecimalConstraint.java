package com.worldofbooks.listingsreport.database.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
    @Constraint(validatedBy = DecimalValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DecimalConstraint {
        String message() default "Price requires 2 decimals";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
