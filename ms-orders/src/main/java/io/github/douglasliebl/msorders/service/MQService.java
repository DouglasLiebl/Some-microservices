package io.github.douglasliebl.msorders.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.douglasliebl.msorders.dto.ProductDTO;

import java.util.Set;

public interface MQService {
    void verifyAvailability(Set<ProductDTO> products) throws JsonProcessingException;
}
