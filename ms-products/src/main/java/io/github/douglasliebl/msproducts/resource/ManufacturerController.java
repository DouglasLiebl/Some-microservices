package io.github.douglasliebl.msproducts.resource;

import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import io.github.douglasliebl.msproducts.services.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/manufacturer")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService service;

    @PostMapping
    public ResponseEntity create(@RequestBody ManufacturerDTO request) {
        var response = service.registerManufacturer(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getManufacturerById(@PathVariable Long id) {
        var response = service.getManufacturerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteManufacturer(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity getProductsByManufacturer(@PathVariable Long id, Pageable pageRequest) {
        var response = service.getProductsByManufacturer(id, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
