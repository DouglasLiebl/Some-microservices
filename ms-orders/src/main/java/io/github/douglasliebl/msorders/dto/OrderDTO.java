package io.github.douglasliebl.msorders.dto;

import io.github.douglasliebl.msorders.model.entity.Status;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private UUID id;
    private Long user_id;
    private Set<ProductDTO> products;
    private Status status;
    private AddressDTO deliveryAddress;

}
