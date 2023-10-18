package io.github.douglasliebl.msproducts.resource;

import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.services.CategoryService;
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

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity create(@RequestBody CategoryDTO request) {
        var response = service.registerCategory(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CategoryDTO request) {
       var response = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id) {
         var response = service.getById(id);
         return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity find(String name, Pageable pageRequest) {
        List<CategoryDTO> response = service.find(name, pageRequest).stream()
                .map(CategoryDTO::of)
                .toList();

        PageImpl<CategoryDTO> pagedResponse = new PageImpl<>(response, pageRequest, response.size());

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse);
    }

}
