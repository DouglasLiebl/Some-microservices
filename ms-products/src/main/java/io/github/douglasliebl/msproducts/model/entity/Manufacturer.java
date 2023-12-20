package io.github.douglasliebl.msproducts.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cnpj;
    private String email;
    private String phoneNumber;

    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Set<Product> product;

    public static Manufacturer of(ManufacturerDTO manufacturerDTO) {
        return Manufacturer.builder()
                .name(manufacturerDTO.getName())
                .cnpj(manufacturerDTO.getCnpj())
                .email(manufacturerDTO.getEmail())
                .phoneNumber(manufacturerDTO.getPhoneNumber())
                .build();
    }

    public static Manufacturer ofDTO(ManufacturerDTO manufacturerDTO) {
        return Manufacturer.builder()
                .id(manufacturerDTO.getId())
                .name(manufacturerDTO.getName())
                .cnpj(manufacturerDTO.getCnpj())
                .email(manufacturerDTO.getEmail())
                .phoneNumber(manufacturerDTO.getPhoneNumber())
                .build();
    }

}
