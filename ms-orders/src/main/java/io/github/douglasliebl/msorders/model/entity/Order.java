package io.github.douglasliebl.msorders.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user_id;
    private Long address_id;

    private Set<Long> products;

    @Enumerated(EnumType.STRING)
    private Status status;


}
