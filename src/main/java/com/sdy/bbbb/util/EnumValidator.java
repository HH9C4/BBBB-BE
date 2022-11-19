package com.sdy.bbbb.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        System.out.println(enumValues.length);
        System.out.println("들어오나 !?!!???!?!!?!!?!!??!!??!!?!?!??");
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
