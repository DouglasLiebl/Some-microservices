package io.github.douglasliebl.msusers.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInsertDTO {

    @NotBlank @NotNull
    private String firstName;
    @NotBlank @NotNull
    private String lastName;

    @Max(11) @Min(11)
    private String cpf;
    @Email
    private String email;
    @NotBlank @NotNull
    @Min(8)
    private String password;
}
