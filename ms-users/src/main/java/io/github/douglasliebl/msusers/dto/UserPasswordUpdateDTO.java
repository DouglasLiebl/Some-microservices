package io.github.douglasliebl.msusers.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordUpdateDTO {

    private String password;
}
