package com.careerdevs.gorestfinal3.validation;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.HashMap;
import java.util.Map;

public class ValidationError {

    private HashMap<String, String> errors = new HashMap<>();

    public void addError (String key, String errorMsg) {

        errors.put(key, errorMsg);
    }

    public boolean hasError() {
        return errors.size() != 0;
    }

    @Override
    public String toString() {
        String errorMessage = "ValidationError: \n";
        for(Map.Entry<String, String> err : errors.entrySet()) {
            errorMessage += err.getKey() + ": " + err.getValue() + "\n";
        }
        return errorMessage;
    }
//    public String toJSONString () {
//        return new JSONPObject(errors).toString();
//    }
}

