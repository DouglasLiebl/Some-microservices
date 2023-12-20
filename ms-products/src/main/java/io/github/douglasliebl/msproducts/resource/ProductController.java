package io.github.douglasliebl.msproducts.resource;

import io.github.douglasliebl.msproducts.configuration.security.anotations.AdminPrivileges;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @AdminPrivileges
    @PostMapping
    public ResponseEntity createProduct(@RequestBody ProductInsertDTO request) {
        var response = service.createProduct(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @AdminPrivileges
    @PutMapping(value = "/{id}")
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO request) {
        var updatedProduct = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getProductById(@PathVariable Long id) {
        var response = (service.getById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AdminPrivileges
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity find(String name, Pageable pageRequest) {
        var response = service.find(name, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
