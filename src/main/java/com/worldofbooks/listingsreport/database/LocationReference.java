package com.worldofbooks.listingsreport.database;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
    @Constraint(validatedBy = LocationValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LocationReference {
        String message() default "Not location reference";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}


