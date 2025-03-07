package com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {
    String message() default "Invalid date range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String startDate();
    String endDate();
}