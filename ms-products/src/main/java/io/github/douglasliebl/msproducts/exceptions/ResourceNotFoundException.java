package io.github.douglasliebl.msproducts.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String message;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }
}
