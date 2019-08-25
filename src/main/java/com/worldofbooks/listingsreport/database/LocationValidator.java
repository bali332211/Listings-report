package com.worldofbooks.listingsreport.database;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationValidator implements
    ConstraintValidator<LocationReference, String> {

    private LocationRepository locationRepository;

    @Autowired
    public LocationValidator(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void initialize(LocationReference id) {
    }

    @Override
    public boolean isValid(String id,
                           ConstraintValidatorContext cxt) {
        return locationRepository.existsById(id);
    }
}
