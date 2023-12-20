package io.github.douglasliebl.msproducts.services.impl;

import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.exception.ResourceNotFoundException;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import io.github.douglasliebl.msproducts.model.repositories.ManufacturerRepository;
import io.github.douglasliebl.msproducts.services.ManufacturerService;
import io.github.douglasliebl.msproducts.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository repository;
    private final ProductService productService;

    @Override
    public ManufacturerDTO registerManufacturer(ManufacturerDTO request) {
        uniqueVerifier(request);
        return ManufacturerDTO.of(repository
                .save(Manufacturer.of(request)));
    }

    @Override
    public ManufacturerDTO getManufacturerById(Long id) {
        return ManufacturerDTO.of(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer not found with id: " + id)));
    }

    @Override
    public String delete(Long id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer not found with id: " + id)));

        return "Manufacturer successfully deleted.";
    }

    @Override
    public PageImpl<ProductDTO> getProductsByManufacturer(Long id, Pageable pageRequest) {
        List<ProductDTO> response = productService
                .findByManufacturer(id, pageRequest).stream()
                .map(ProductDTO::of)
                .toList();

        return new PageImpl<>(response, pageRequest, response.size());
    }

    private void uniqueVerifier(ManufacturerDTO request) {
        if (repository.existsByName(request.getName())) throw new DataIntegrityViolationException("Manufacturer name already used.");
        if (repository.existsByCnpj(request.getCnpj())) throw new DataIntegrityViolationException("CNPJ already registered.");
        if (repository.existsByEmail(request.getEmail())) throw new DataIntegrityViolationException("Email already registered.");
        if (repository.existsByPhoneNumber(request.getPhoneNumber())) throw new DataIntegrityViolationException("PhoneNumber already registered.");
    }
}

