package org.ubb.image_handler_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends RuntimeException
{
    public UnauthorizedException(String message)
    {
        super(message);
    }
}
