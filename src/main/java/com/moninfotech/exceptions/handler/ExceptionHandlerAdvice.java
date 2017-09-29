package com.moninfotech.exceptions.handler;

import com.moninfotech.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(NotFoundException.class)
    private ModelAndView handleNotFoundException(NotFoundException ex){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/error_logical");
        modelAndView.addObject("error",ex.getMessage());
        return modelAndView;
    }

}
