package org.ubb.image_handler_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ProcessingException extends RuntimeException
{
    public ProcessingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
