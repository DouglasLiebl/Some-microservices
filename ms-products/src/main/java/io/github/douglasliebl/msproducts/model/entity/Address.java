package io.github.douglasliebl.msproducts.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_manufacturer_addresses")
public class Address {

    @Id
    @JoinColumn(name = "manufacturer_id")
    @ManyToOne
    @JsonManagedReference
    private Manufacturer id;

    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;
}
