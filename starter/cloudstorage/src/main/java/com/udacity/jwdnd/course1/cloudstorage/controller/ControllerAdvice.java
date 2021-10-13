package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MultipartException.class)
    public String maxSizeException(Model model) {
        model.addAttribute("error", "The file size is bigger than 5mb.");

        return "/result";
    }
}
