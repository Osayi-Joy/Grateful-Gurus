package com.decagon.rewardyourteacher.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ErrorsMap {
    public static Map<String, String> getErrors(BindingResult result){
        if(result.hasErrors()){
            Map<String, String> errorsMap = new HashMap<>();
            for(FieldError error : result.getFieldErrors()){
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return errorsMap;
        }
        return null;
    }

}
