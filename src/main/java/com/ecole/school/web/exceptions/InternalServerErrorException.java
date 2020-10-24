package com.ecole.school.web.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Data
@AllArgsConstructor
public class InternalServerErrorException extends RuntimeException {
    private String message;
}
