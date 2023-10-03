package io.github.douglasliebl.msproducts.services.impl;

import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import io.github.douglasliebl.msproducts.model.repositories.ManufacturerRepository;
import io.github.douglasliebl.msproducts.services.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository repository;

    @Override
    public Manufacturer registerManufacturer(Manufacturer request) {
        uniqueVerifier(request);
        return repository.save(request);
    }

    @Override
    public Optional<Manufacturer> getManufacturerById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Manufacturer request) {
        repository.delete(request);
    }

    private void uniqueVerifier(Manufacturer request) {
        if (repository.existsByName(request.getName())) throw new DataIntegrityViolationException("Manufacturer name already used.");
        if (repository.existsByCnpj(request.getCnpj())) throw new DataIntegrityViolationException("CNPJ already registered.");
        if (repository.existsByEmail(request.getEmail())) throw new DataIntegrityViolationException("Email already registered.");
        if (repository.existsByPhoneNumber(request.getPhoneNumber())) throw new DataIntegrityViolationException("PhoneNumber already registered.");
    }
}

