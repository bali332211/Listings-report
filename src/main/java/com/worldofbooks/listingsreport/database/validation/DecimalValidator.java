package com.worldofbooks.listingsreport.database.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DecimalValidator implements
    ConstraintValidator<DecimalConstraint, Double> {

    @Override
    public void initialize(DecimalConstraint listingPrice) {
    }

    @Override
    public boolean isValid(Double listingPrice,
                           ConstraintValidatorContext cxt) {
        String[] splitNumber = listingPrice.toString().split("\\.");
        int decimalLength = splitNumber[1].length();

        return decimalLength == 2;
    }
}
