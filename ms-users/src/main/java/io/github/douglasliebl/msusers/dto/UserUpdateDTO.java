package io.github.douglasliebl.msusers.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    private String firstName;
    private String lastName;
    private String email;

}
