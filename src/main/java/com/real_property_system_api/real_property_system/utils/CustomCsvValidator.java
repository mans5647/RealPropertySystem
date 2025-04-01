package com.real_property_system_api.real_property_system.utils;

import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.LineValidator;
import com.real_property_system_api.real_property_system.models.User;

public class CustomCsvValidator implements LineValidator
{

    @Override
    public boolean isValid(String line) {
        return line.endsWith("\r\n");
    }

    @Override
    public void validate(String line) throws CsvValidationException 
    {
        if (!isValid(line)) throw new CsvValidationException("Invalid Line");
    }
    
}