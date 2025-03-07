package com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private String startDateFieldName;
    private String endDateFieldName;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        startDateFieldName = constraintAnnotation.startDate();
        endDateFieldName = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Date startDate = null;
            Date endDate = null;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
            String sdate  = (String) beanWrapper.getPropertyValue(startDateFieldName);
            String cdate = (String) beanWrapper.getPropertyValue(endDateFieldName);

            //startDate = (Date) beanWrapper.getPropertyValue(startDateFieldName);
            //endDate = (Date) beanWrapper.getPropertyValue(endDateFieldName);
            startDate = formatter.parse(sdate);
            endDate = formatter.parse(cdate);

            if (startDate == null || endDate == null) {
                return false;
            }

            return endDate.after(startDate);
        } catch (Exception e) {
            return false;
        }
    }
}