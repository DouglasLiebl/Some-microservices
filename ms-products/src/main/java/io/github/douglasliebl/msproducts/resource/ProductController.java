package io.github.douglasliebl.msproducts.resource;

import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.model.entity.Product;
import io.github.douglasliebl.msproducts.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity createProduct(@RequestBody ProductInsertDTO request) {
        var response = mapper.map(service.createProduct(request), ProductDTO.class);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO request) {
        Product actualProduct = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var updatedProduct = mapper.map(service.update(actualProduct, request), ProductDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getProductById(@PathVariable Long id) {
        var response = mapper.map(service.getById(id), ProductDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteProduct(@PathVariable Long id) {
        service.delete(service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping
    public ResponseEntity find(String name, Pageable pageRequest) {
        var result = service.find(name, pageRequest);
        List<ProductDTO> response = result.stream()
                .map(entity -> mapper.map(entity, ProductDTO.class))
                .toList();
        PageImpl<ProductDTO> pagedResponse = new PageImpl<>(response, pageRequest, result.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse);
    }



}
