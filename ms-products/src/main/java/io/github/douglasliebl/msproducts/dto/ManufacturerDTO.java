package io.github.douglasliebl.msproducts.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerDTO {

    private Long id;
    private String name;
    private String cnpj;
    private String email;
    private String phoneNumber;
}
