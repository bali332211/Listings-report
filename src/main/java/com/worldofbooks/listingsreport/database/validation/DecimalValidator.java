package com.worldofbooks.listingsreport.database.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DecimalValidator implements
    ConstraintValidator<DecimalConstraint, Integer> {

    @Override
    public void initialize(DecimalConstraint listingPrice) {
    }

    @Override
    public boolean isValid(Integer listingPrice,
                           ConstraintValidatorContext cxt) {
        return Pattern.matches("\\d+(\\.\\d{2})?", listingPrice.toString());
    }



}
