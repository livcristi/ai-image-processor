package org.ubb.image_handler_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileRetrievalException extends RuntimeException
{
    public FileRetrievalException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
