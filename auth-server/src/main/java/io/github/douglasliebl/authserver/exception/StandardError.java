package io.github.douglasliebl.authserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class StandardError {

    private LocalDateTime localDateTime;
    private Integer status;
    private String error;
    private String path;
}
