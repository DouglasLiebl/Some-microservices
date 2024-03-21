package io.github.douglasliebl.msorders.model.entity;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String zipCode;
    private String street;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;

}
