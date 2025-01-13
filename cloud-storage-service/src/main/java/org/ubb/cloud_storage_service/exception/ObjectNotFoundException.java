package org.ubb.cloud_storage_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException
{
    public ObjectNotFoundException(String message)
    {
        super(message);
    }
}
