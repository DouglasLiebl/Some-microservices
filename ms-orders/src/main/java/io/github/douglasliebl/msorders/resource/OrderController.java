package io.github.douglasliebl.msorders.resource;

import io.github.douglasliebl.msorders.dto.OrderDTO;
import io.github.douglasliebl.msorders.dto.ProductDTO;
import io.github.douglasliebl.msorders.service.MQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MQService mqService;

    @PostMapping("/order")
    public ResponseEntity<?> registerOrder() {
        Set<ProductDTO> list = new HashSet<>();
        list.add(ProductDTO.builder().id(2L).quantity(3L).build());
        list.add(ProductDTO.builder().id(2L).quantity(3L).build());
        list.add(ProductDTO.builder().id(2L).quantity(3L).build());


        mqService.verifyAvailability(list);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ok");
    }
}
