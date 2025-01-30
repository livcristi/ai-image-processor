package org.ubb.image_handler_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameTakenException extends RuntimeException
{
    public UsernameTakenException(String message)
    {
        super(message);
    }
}
