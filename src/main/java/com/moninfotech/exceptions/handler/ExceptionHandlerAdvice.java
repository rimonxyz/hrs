package com.moninfotech.exceptions.handler;

import com.moninfotech.exceptions.NotFoundException;
import com.moninfotech.exceptions.nullexceptions.NullPasswordException;
import com.moninfotech.exceptions.UserAlreadyExistsException;
import com.mysql.jdbc.PacketTooBigException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(NotFoundException.class)
    private ModelAndView handleNotFoundException(NotFoundException ex) {
        return getErrorView(ex);
    }

    @ExceptionHandler(NullPasswordException.class)
    private ModelAndView handleNullPasswordException(NullPasswordException ex) {
        return getErrorView(ex);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ModelAndView handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/login?message=" + ex.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(MultipartException.class)
    private ModelAndView handleFileSizeLimitExceedException(Throwable ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/dashboard?message=File size can not be greater than 1MB!");
        return modelAndView;
    }
    @ExceptionHandler(PacketTooBigException.class)
    private ModelAndView handlePacketTooBigExceptionException(Throwable ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/dashboard?message=File size can not be greater than 1MB!");
        return modelAndView;
    }




    private ModelAndView getErrorView(Throwable throwable) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/error_logical");
        modelAndView.addObject("error", throwable.getMessage());
        return modelAndView;
    }

}
