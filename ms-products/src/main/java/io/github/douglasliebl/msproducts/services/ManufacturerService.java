package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;

import java.util.Optional;

public interface ManufacturerService {

    ManufacturerDTO registerManufacturer(ManufacturerDTO request);

    ManufacturerDTO getManufacturerById(Long id);

    String delete(Long id);
}
