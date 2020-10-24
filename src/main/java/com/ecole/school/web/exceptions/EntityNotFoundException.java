package com.ecole.school.web.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Data
@AllArgsConstructor
public class EntityNotFoundException extends RuntimeException {
    private String message;
}
