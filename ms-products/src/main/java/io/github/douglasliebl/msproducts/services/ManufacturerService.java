package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.ManufacturerDTO;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface ManufacturerService {

    ManufacturerDTO registerManufacturer(ManufacturerDTO request);

    ManufacturerDTO getManufacturerById(Long id);

    String delete(Long id);

    PageImpl<ProductDTO> getProductsByManufacturer(Long id, Pageable pageRequest);
}
