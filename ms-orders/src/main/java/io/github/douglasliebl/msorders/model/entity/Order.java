package io.github.douglasliebl.msorders.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Orders")
public class Order {

    @Id
    private UUID id;

    private Long user_id;
    private Set<Product> products;
    private Status status;
    private Address deliveryAddress;

}
