package com.ybritto.milestory.goal.in.controller;

import com.ybritto.milestory.goal.application.usecase.GoalCategoryNotFoundException;
import com.ybritto.milestory.goal.application.usecase.GoalNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GoalControllerExceptionHandler {

    @ExceptionHandler(GoalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleGoalNotFound() {
    }

    @ExceptionHandler(GoalCategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleGoalCategoryNotFound() {
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleIllegalArgumentException() {
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    void handleIllegalStateException() {
    }
}
