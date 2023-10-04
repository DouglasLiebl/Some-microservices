package io.github.douglasliebl.msproducts.resource;

import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import io.github.douglasliebl.msproducts.model.entity.Product;
import io.github.douglasliebl.msproducts.services.ManufacturerService;
import io.github.douglasliebl.msproducts.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/manufacturer")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService service;
    private final ProductService productService;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity create(@RequestBody ManufacturerDTO request) {
        var response = mapper.map(service
                .registerManufacturer(mapper.map(request, Manufacturer.class)), ManufacturerDTO.class);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getManufacturerById(@PathVariable Long id) {
        var response = service.getManufacturerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManufacturer(@PathVariable Long id) {
        service.delete(service.getManufacturerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity getProductsByManufacturer(@PathVariable Long id, Pageable pageRequest) {
        var manufacturer = service.getManufacturerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Page<Product> result = productService.findByManufacturer(manufacturer, pageRequest);
        List<ProductDTO> response = result.stream()
                .map(entity -> mapper.map(entity, ProductDTO.class))
                .toList();

        PageImpl<ProductDTO> pagedResponse = new PageImpl<>(response, pageRequest, result.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse);
    }
}
