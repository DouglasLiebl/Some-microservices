package io.github.douglasliebl.msproducts.resource;

import io.github.douglasliebl.msproducts.configuration.security.anotations.AdminPrivileges;
import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import io.github.douglasliebl.msproducts.services.ManufacturerService;
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
@RequestMapping("/manufacturer")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService service;
    private final ProductService productService;

    @AdminPrivileges
    @PostMapping
    public ResponseEntity create(@RequestBody ManufacturerDTO request) {
        var response = service.registerManufacturer(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @AdminPrivileges
    @GetMapping(value = "/{id}")
    public ResponseEntity getManufacturerById(@PathVariable Long id) {
        var response = service.getManufacturerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AdminPrivileges
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteManufacturer(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AdminPrivileges
    @GetMapping("/{id}/products")
    public ResponseEntity getProductsByManufacturer(@PathVariable Long id, Pageable pageRequest) {
        List<ProductDTO> response = productService
                .findByManufacturer(Manufacturer.of(service.getManufacturerById(id)), pageRequest).stream()
                .map(ProductDTO::of)
                .toList();

        PageImpl<ProductDTO> pagedResponse = new PageImpl<>(response, pageRequest, response.size());

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse);
    }
}
