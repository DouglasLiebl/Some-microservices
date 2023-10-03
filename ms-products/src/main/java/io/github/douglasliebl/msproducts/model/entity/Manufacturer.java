package io.github.douglasliebl.msproducts.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Product> product;

}
