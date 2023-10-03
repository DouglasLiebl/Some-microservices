package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.model.entity.Manufacturer;

import java.util.Optional;

public interface ManufacturerService {

    Manufacturer registerManufacturer(Manufacturer request);

    Optional<Manufacturer> getManufacturerById(Long id);

    void delete(Manufacturer manufacturer);
}
