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
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity create(@RequestBody CategoryDTO request) {
        var response = mapper.map(service.registerCategory(mapper.map(request, Category.class)), CategoryDTO.class);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CategoryDTO request) {
        Category actualCategory = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var updatedCategory = mapper.map(service.update(actualCategory, request), CategoryDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCategory(@PathVariable Long id) {
        service.delete(service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id) {
         var response = mapper.map(service.getById(id), CategoryDTO.class);
         return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity find(String name, Pageable pageRequest) {
        var result = service.find(name, pageRequest);
        List<CategoryDTO> response = result.stream()
                .map(entity -> mapper.map(entity, CategoryDTO.class))
                .toList();
        PageImpl<CategoryDTO> pagedResponse = new PageImpl<>(response, pageRequest, result.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse);
    }

}
