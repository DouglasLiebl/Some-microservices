package io.github.douglasliebl.msproducts.resource;

import io.github.douglasliebl.msproducts.configuration.security.anotations.AdminPrivileges;
import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.services.CategoryService;
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
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @AdminPrivileges
    @PostMapping
    public ResponseEntity create(@RequestBody CategoryDTO request) {
        var response = service.registerCategory(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @AdminPrivileges
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CategoryDTO request) {
       var response = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AdminPrivileges
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity getProductsByCategory(@PathVariable Long id, Pageable pageRequest) {
        var response = service.getProductsByCategory(id, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity getAllCategories() {
        var response = service.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
