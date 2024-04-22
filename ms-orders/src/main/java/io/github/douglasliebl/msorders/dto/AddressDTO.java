package io.github.douglasliebl.msorders.dto;

import lombok.*;

@Setter @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String zipCode;
    private String street;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
}
