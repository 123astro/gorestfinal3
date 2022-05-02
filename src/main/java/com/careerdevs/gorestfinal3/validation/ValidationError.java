package com.careerdevs.gorestfinal3.validation;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ValidationError {

    private final HashMap<String, String> errors = new HashMap<>();

    public void addError (String key, String errorMsg) { errors.put(key, errorMsg);}

    public boolean hasError() {return errors.size() != 0;}

    public boolean hasSpecificError (String key){ return errors.get(key) != null;}


    @Override
    public String toString() {
        StringBuilder  errorMessage = new StringBuilder("ValidationError:\n");
        for(Map.Entry<String, String> err : errors.entrySet()) {
            errorMessage.append(err.getKey()).append(": ").append(err.getValue()).append("\n");
        }
        return errorMessage.toString();
    }

    public String toJSONString () {
        return new JSONObject(errors).toString();
    }

    public void checkIsPresent(String key, String value){
        if(value == null || value.trim().equals("")){
            addError(key, key+ " can not be left blank");
        }
    }
}

