package com.worldofbooks.listingsreport.database.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDValidator implements
    ConstraintValidator<UUIDConstraint, String> {

    @Override
    public void initialize(UUIDConstraint id) {
    }

    @Override
    public boolean isValid(String id,
                           ConstraintValidatorContext cxt) {
        return id != null && isUUID(id);
    }

    private boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
